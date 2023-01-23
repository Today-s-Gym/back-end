package com.gym.post;

import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponseStatus;
import com.gym.post.dto.GetPostsListRes;
import com.gym.post.like.LikeService;
import com.gym.record.Record;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public List<GetPostsListRes> getPostsByCategoryId(Integer userId, Integer categoryId) throws BaseException {
        List<Post> posts = postRepository.findByCategoryId(categoryId, PageRequest.of(0, 10)).orElse(null);
        if(posts == null) {
            throw new BaseException(BaseResponseStatus.EMPTY_CATEGORY);
        }

        List<GetPostsListRes> postsListRes = new ArrayList<>();

        User viewer = utilService.findByUserIdWithValidation(userId);

        for (int i=0; i<posts.size(); i++) {
            Post post = posts.get(i);

            //삭제된 게시물이라면 넘김
            if(post.isDeleted())    continue;

            //해당 게시물에 첨부된 기록이 없을 수도 있기에 default 값 지정
            Integer recordId = 0;
            String recordPhotoImgUrl = "기록이 없습니다.";
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
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(convertLocalDateTimeToTime(post.getCreatedAt()))
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

}
