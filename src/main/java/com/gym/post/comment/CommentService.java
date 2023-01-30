package com.gym.post.comment;

import com.gym.config.exception.BaseException;
import com.gym.post.Post;
import com.gym.post.comment.dto.GetCommentsRes;
import com.gym.post.comment.dto.PostCommentReq;
import com.gym.user.User;
import com.gym.user.UserService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UtilService utilService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public String createComment(Integer userId, PostCommentReq postCommentReq) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        Post post = utilService.findByPostIdWithValidation(postCommentReq.getPostId());

        Comment comment = Comment.builder()
                .content(postCommentReq.getContent())
                .user(user)
                .post(post)
                .build();

        save(comment);

        return "commentId: " + comment.getCommentId() + "인 댓글을 생성했습니다.";
    }
    /***
     * GET : 특정 게시글의 댓글 모두 얻어오기
     * @param postId
     * @return List<GetCommentsRes>
     */
    @Transactional
    public List<GetCommentsRes> getCommentsByPostId(Integer userId, Integer postId) throws BaseException {
        Post post = utilService.findByPostIdWithValidation(postId);

        //가져온 post의 comment 리스트
        List<Comment> comments = post.getCommentList();
        List<GetCommentsRes> res = new ArrayList<>();

        for(int i = 0; i < comments.size() ; i++) {
            Comment comment = comments.get(i);
            //댓글을 작성한 유저
            User writer = comment.getUser();

            boolean isMine = false; // 댓글이 내것인지 나타내는 변수
            if (userId.equals(writer.getUserId())) {
                isMine = true;
            }

            res.add(new GetCommentsRes(comment.getCommentId(), comment.getContent(), writer.getUserId(), writer.getNickName(), userService.getNowAvatarImg(writer.getUserId()), isMine));
        }
        return res;
    }

    @Transactional
    public String deleteComment(Integer userId, Integer commentId) throws BaseException {
        Comment comment = utilService.findByCommentIdWithValidation(commentId);

        //댓글을 작성한 유저
        User writer = comment.getUser();
        //삭제하려는 유저
        User viewer = utilService.findByUserIdWithValidation(userId);

        //같아야만 삭제 가능
        if(writer.getUserId() == viewer.getUserId()) {
            //댓글 삭제
            commentRepository.deleteComment(commentId);
            return "commentId: " + comment.getCommentId() + "인 댓글을 삭제했습니다.";
        } else {
            return "자신의 댓글만 삭제할 수 있습니다.";
        }
    }

}
