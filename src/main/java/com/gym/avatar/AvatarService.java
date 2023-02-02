package com.gym.avatar;

import com.gym.avatar.avatar.AvatarStep;
import com.gym.avatar.avatar.MyAvatar;
import com.gym.avatar.avatar.MyAvatarRepository;
import com.gym.avatar.myAvatarCollection.MyAvatarCollection;
import com.gym.avatar.myAvatarCollection.MyAvatarCollectionRepository;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService {
    private final UserRepository userRepository;
    private final MyAvatarRepository myAvatarRepository;
    private final MyAvatarCollectionRepository myAvatarCollectionRepository;

    @Transactional
    public boolean checkAndMyAvatarLevelUp(Integer userId) {
        log.info("레벨업 확인 user : {}", userId);
        User user = userRepository.findWithMyAvatarByUserId(userId);
        int recordCount = user.getRecordCount();
        AvatarStep avatarStep = AvatarStep.findByRecordCount(recordCount);

        if (!user.getMyAvatar().getAvatarStep().equals(avatarStep)) {
            log.info("레벨업 조건 만족 : {}", avatarStep);
            userLevelUp(user, avatarStep);
            return true;
        }
        log.info("레벨업 조건 미달");
        return false;
    }

    @Transactional
    public void setInitialAvatar(User user) {
        log.info("최초 로그인 - 초기 아바타 설정");
        MyAvatar initialMyAvatar = getInitialMyAvatar();
        changeAvatarStep(user, initialMyAvatar);
    }

    @Transactional
    public void userLevelUp(User user, AvatarStep avatarStep) {
        MyAvatar levelUpAvatar = myAvatarRepository.findByAvatarStep(avatarStep).get();
        changeAvatarStep(user, levelUpAvatar);
    }

    private void changeAvatarStep(User user, MyAvatar myAvatar) {
        user.changeAvatarStep(myAvatar);
        saveMyAvatarInCollection(user, myAvatar);
    }

    private void saveMyAvatarInCollection(User user, MyAvatar myAvatar) {
        MyAvatarCollection myAvatarCollection = new MyAvatarCollection();
        myAvatarCollection.setUser(user);
        myAvatarCollection.setMyAvatar(myAvatar);
        myAvatarCollectionRepository.save(myAvatarCollection);
    }
    private MyAvatar getInitialMyAvatar() {
        return myAvatarRepository.findByAvatarStep(AvatarStep.getInitialAvatarStep()).get();
    }
}