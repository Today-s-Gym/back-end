package com.gym.post;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.category.Category;
import com.gym.post.dto.GetMyPostsListRes;
import com.gym.post.dto.GetPostRes;
import com.gym.post.dto.GetPostsListRes;
import com.gym.post.dto.PostPostReq;
import com.gym.post.like.LikeService;
import com.gym.post.photo.PostPhoto;
import com.gym.post.photo.PostPhotoService;
import com.gym.record.Record;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import com.gym.utils.S3Service;
import com.gym.utils.UtilService;
import com.gym.utils.dto.getS3Res;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.gym.utils.UtilService.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final UtilService utilService;
    private final PostPhotoService postPhotoService;
    private final UserService userService;
    private final S3Service s3Service;


    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }



    @Transactional
    public String createPost(Integer userId, PostPostReq postPostReq, List<MultipartFile> multipartFiles) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        Category category = utilService.findByCategoryIdWithValidation(postPostReq.getCategoryId());

        Record record = null;
        //첨부된 기록이 있으면
        if(postPostReq.getRecordId() != null) {
            record = utilService.findByRecordIdWithValidation(postPostReq.getRecordId());
        }

        Post post = Post.builder()
                .category(category)
                .title(postPostReq.getTitle())
                .content(postPostReq.getContent())
                .record(record)
                .user(user)
                .photoList(new ArrayList<>())
                .commentList(new ArrayList<>())
                .build();

        save(post);

        if(multipartFiles != null) {
            List<getS3Res> imgUrls = s3Service.uploadFile(multipartFiles);
            postPhotoService.saveAllPostPhotoByPost(imgUrls, post);
        }


        return "postId: " + post.getPostId() + "인 게시글을 생성했습니다.";
    }

    public List<GetPostsListRes> getPostsByCategoryId(Integer userId, Integer categoryId) throws BaseException {
        Category category = utilService.findByCategoryIdWithValidation(categoryId);

        List<Post> posts = postRepository.findByCategoryId(category, PageRequest.of(0, 10)).orElse(null);
        if(posts == null) {
            throw new BaseException(BaseResponseStatus.EMPTY_CATEGORY);
        }

        List<GetPostsListRes> postsListRes = new ArrayList<>();

        User viewer = utilService.findByUserIdWithValidation(userId);

        for (int i=0; i<posts.size(); i++) {
            Post post = posts.get(i);

            //해당 게시물에 첨부된 기록이 없을 수도 있기에 default 값 지정
            Integer recordId = 0;
            String recordPhotoImgUrl = returnRecordBaseImage();
            String recordCreatedAt = "기록이 없습니다.";
            String recordContent = "기록이 없습니다.";

            //기록이 첨부되어 있다면
            if(post.getRecord() != null) {
                Record record = post.getRecord();
                recordId = record.getRecordId();
                recordPhotoImgUrl = returnRecordBaseImage();
                recordCreatedAt = convertLocalDateTimeToLocalDate(record.getCreatedAt());
                recordContent = record.getContent();
            }

            GetPostsListRes res = GetPostsListRes.builder()
                    .categoryName(category.getName())
                    .postId(post.getPostId())
                    .postPhotoList(postPhotoService.findAllPhotosByPostId(post.getPostId()))
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(convertLocalDateTimeToTime(post.getCreatedAt()))
                    .writerId(post.getUser().getUserId())
                    .writerAvatarImgUrl(userService.getNowAvatarImg(post.getUser().getUserId()))
                    .writerNickName(post.getUser().getNickName())
                    .recordId(recordId)
                    .recordPhotoImgUrl(recordPhotoImgUrl)
                    .recordCreatedAt(recordCreatedAt)
                    .recordContent(recordContent)
                    .likeCounts(likeService.getLikeCounts(post.getPostId()))
                    .liked(likeService.checkLike(viewer.getUserId(), post.getPostId()))
                    .commentCounts(post.getCommentList().size())
                    .build();

            postsListRes.add(res);
        }

        return postsListRes;
    }

    /**
     * 게시글 업데이트
     */
    @Transactional
    @Modifying
    public String updatePost(Integer userId, Integer postId, PostPostReq postPostReq, List<MultipartFile> multipartFiles) throws BaseException {
        Post post = utilService.findByPostIdWithValidation(postId);
        //게시글을 작성한 유저
        User writer = post.getUser();
        //수정하려는 유저
        User viewer = utilService.findByUserIdWithValidation(userId);

        //자신의 게시글이 맞다면
        if(writer.getUserId() == viewer.getUserId()) {
            //게시글 title, content 업데이트
            post.updatePost(postPostReq.getTitle(), postPostReq.getContent());

            //사진 업데이트, 지우고 다시 저장!
            List<PostPhoto> allByPostId = postPhotoService.findAllByPostId(postId);
            postPhotoService.deleteAllPostPhotos(allByPostId);
            List<Integer> Ids = postPhotoService.findAllId(post.getPostId());
            postPhotoService.deleteAllPostPhotoByPost(Ids);

            if(multipartFiles != null) {
                List<getS3Res> imgUrls = s3Service.uploadFile(multipartFiles);
                postPhotoService.saveAllPostPhotoByPost(imgUrls, post);
            }

            return "postId: " + post.getPostId() + "인 게시글을 수정했습니다.";
        } else {
            return "자신의 게시글만 삭제할 수 있습니다.";
        }
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    @Modifying
    public String deletePost(Integer userId, Integer postId) throws BaseException {
        Post post = utilService.findByPostIdWithValidation(postId);
        //게시글을 작성한 유저
        User writer = post.getUser();
        //수정하려는 유저
        User viewer = utilService.findByUserIdWithValidation(userId);
        if(writer.getUserId() == viewer.getUserId()) {
            //postPhoto 삭제
            List<PostPhoto> allByPostId = postPhotoService.findAllByPostId(postId);
            postPhotoService.deleteAllPostPhotos(allByPostId);
            List<Integer> Ids = postPhotoService.findAllId(post.getPostId());
            postPhotoService.deleteAllPostPhotoByPost(Ids);
            //post 삭제
            postRepository.deleteByPostId(post.getPostId());
            return "postId: " + post.getPostId() + "인 게시글을 삭제했습니다.";
        } else {
            return "자신의 게시글만 삭제할 수 있습니다.";
        }
    }

    public GetPostRes getPostByPostId(Integer userId, Integer postId) throws BaseException {
        Post post = utilService.findByPostIdWithValidation(postId);
        //게시글을 보려는 유저
        User viewer = utilService.findByUserIdWithValidation(userId);
        //게시글을 작성한 유저
        User writer = post.getUser();

        //자신의 게시글인지 판단하는 boolean 값
        boolean isMine = false;

        //같으면 isMine 을 true 로
        if(checkIsMine(viewer, writer))
            isMine = true;

        //해당 게시물에 첨부된 기록이 없을 수도 있기에 default 값 지정
        Integer recordId = 0;
        String recordPhotoImgUrl = returnRecordBaseImage();
        String recordCreatedAt = "기록이 없습니다.";
        String recordContent = "기록이 없습니다.";

        //기록이 첨부되어 있다면
        if(post.getRecord() != null) {
            Record record = post.getRecord();
            recordId = record.getRecordId();
            recordPhotoImgUrl = returnRecordBaseImage();
            recordCreatedAt = convertLocalDateTimeToLocalDate(record.getCreatedAt());
            recordContent = record.getContent();
        }

        GetPostsListRes res = GetPostsListRes.builder()
                .categoryName(post.getCategory().getName())
                .postId(post.getPostId())
                .postPhotoList(postPhotoService.findAllPhotosByPostId(post.getPostId()))
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(convertLocalDateTimeToTime(post.getCreatedAt()))
                .writerId(post.getUser().getUserId())
                .writerAvatarImgUrl(userService.getNowAvatarImg(post.getUser().getUserId()))
                .writerNickName(post.getUser().getNickName())
                .recordId(recordId)
                .recordPhotoImgUrl(recordPhotoImgUrl)
                .recordCreatedAt(recordCreatedAt)
                .recordContent(recordContent)
                .likeCounts(likeService.getLikeCounts(post.getPostId()))
                .liked(likeService.checkLike(viewer.getUserId(), post.getPostId()))
                .commentCounts(post.getCommentList().size())
                .build();

        GetPostRes postRes = GetPostRes.builder()
                .getPostRes(res)
                .isMine(isMine)
                .build();

        return postRes;
    }

    public boolean checkIsMine(User viewer, User writer) {
        if(writer.getUserId() == viewer.getUserId()) return true;
        else return false;
    }


    public List<GetMyPostsListRes> getMyPosts(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        List<Post> posts = user.getPostList();

        List<GetMyPostsListRes> myPosts = new ArrayList<>();
        for(int i=0; i<posts.size(); i++) {
            Post post = posts.get(i);

            GetMyPostsListRes res = GetMyPostsListRes.builder()
                    .categoryName(post.getCategory().getName())
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(convertLocalDateTimeToLocalDate(post.getCreatedAt()))
                    .liked(likeService.checkLike(userId, post.getPostId()))
                    .commentCounts(post.getCommentList().size())
                    .postPhotoImgUrl(postPhotoService.findFirstByPostId(post.getPostId()))
                    .build();

            myPosts.add(res);
        }
        return myPosts;
    }

}
