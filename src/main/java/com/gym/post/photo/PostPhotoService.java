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

import java.util.ArrayList;
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
     *  여러 개의 PostPhoto 저장
     */
    @Transactional
    public void saveAllPostPhotoByPost(PostPostReq postPostReq, Post post) {
        // PostPhoto 리스트를 받아옴
        List<String> imgUrlList = postPostReq.getPostPhotos();

        List<PostPhoto> postPhotos = new ArrayList<>();
        for (String photo : imgUrlList) {
            PostPhoto newPostPhoto = PostPhoto.builder()
                    .imgUrl(photo).build();

            postPhotos.add(newPostPhoto);
            post.addPhotoList(newPostPhoto);
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

    /**
     * 게시글와 연관된 모든 id 조회
     */
    public List<Integer> findAllId(int postId){
        return postPhotoRepository.findAllId(postId);
    }

    public String findFirstByPostId(Integer postId) {
        PostPhoto res = postPhotoRepository.findFirstBy(postId).orElse(null);
        if(res == null) {
            return "첨부된 사진이 없습니다.";
        } else {
            return res.getImgUrl();
        }
    }
}
