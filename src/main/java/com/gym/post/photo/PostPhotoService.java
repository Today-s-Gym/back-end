package com.gym.post.photo;

import com.gym.post.Post;
import com.gym.post.PostRepository;
import com.gym.post.dto.PostPostReq;
import com.gym.record.Record;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.photo.RecordPhoto;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPhotoService {

    private final PostPhotoRepository postPhotoRepository;

    @Transactional
    public void savePostPhoto(List<PostPhoto> postPhotos){
        postPhotoRepository.saveAll(postPhotos);
    }

    /**
     *  여러개의 PostPhoto 저장
     */
    @Transactional
    public void saveAllPostPhotoByPost(PostPostReq postPostReq, Post post) {
        // PostPhoto 리스트를 받아옴
        List<PostPhoto> postPhotos = postPostReq.getPostPhotos();

        for (PostPhoto postPhoto: postPhotos) {
            // 사진 객체에 추가해줌
            post.addPhotoList(postPhoto);
        }

        savePostPhoto(postPhotos);
    }

    /**
     * 게시글과 연관된 모든 postPhoto 삭제
     */
    @Transactional
    public void deleteAllPostPhotoByPost(List<Integer> ids){
        postPhotoRepository.deleteAllByPost(ids);
    }

    /**
     * 게시글과 연관된 모든 postPhoto 의 imgUrl 조회
     */
    public List<String> findAllPhotosByPostId(int postId){
        return postPhotoRepository.findAllPhotos(postId);
    }
}
