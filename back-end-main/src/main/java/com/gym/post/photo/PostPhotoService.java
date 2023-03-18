package com.gym.post.photo;

import com.gym.post.Post;
import com.gym.post.PostRepository;
import com.gym.post.dto.PostPostReq;
import com.gym.record.Record;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.photo.RecordPhoto;
import com.gym.utils.S3Service;
import com.gym.utils.UtilService;
import com.gym.utils.dto.getS3Res;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPhotoService {

    private final PostPhotoRepository postPhotoRepository;
    private final S3Service s3Service;

    @Transactional
    public void savePostPhoto(List<PostPhoto> postPhotos){
        postPhotoRepository.saveAll(postPhotos);
    }

    /**
     *  여러 개의 PostPhoto 저장
     */
    @Transactional
    public void saveAllPostPhotoByPost(List<getS3Res> getS3ResList , Post post) {
        // PostPhoto 리스트를 받아옴

        List<PostPhoto> postPhotos = new ArrayList<>();
        for (getS3Res getS3Res : getS3ResList) {
            PostPhoto newPostPhoto = PostPhoto.builder()
                    .imgUrl(getS3Res.getImgUrl()).fileName(getS3Res.getFileName()).build();
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

    @Transactional
    public void deleteAllPostPhotos(List<PostPhoto> postPhotos){
        for (PostPhoto recordPhoto : postPhotos) {
            s3Service.deleteFile(recordPhoto.getFileName());
        }
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

    public List<PostPhoto> findAllByPostId(Integer postId){
        return postPhotoRepository.findAllByPostId(postId).orElse(null);
    }

    public String findFirstByPostId(Integer postId) {
        List<PostPhoto> pp = postPhotoRepository.findAllByPostId(postId).orElse(null);

        if(pp.size() == 0) {
            return "첨부된 사진이 없습니다.";
        } else {
            return pp.get(0).getImgUrl();
        }
    }
}
