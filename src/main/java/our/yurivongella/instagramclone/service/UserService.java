package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.UsersRequestDto;
import our.yurivongella.instagramclone.domain.Follow.Follow;
import our.yurivongella.instagramclone.domain.Follow.FollowRepository;
import our.yurivongella.instagramclone.domain.Users.Users;
import our.yurivongella.instagramclone.domain.Users.UsersRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final FollowRepository followRepository;

    public Long signUp(UsersRequestDto usersRequestDto) {
        Users users = usersRequestDto.toUsers();
        return usersRepository.save(users).getId();
    }

    public boolean follow(Long fromUserId, Long toUserId) {
        Optional<Users> fromUser = usersRepository.findById(fromUserId);
        Optional<Users> toUser = usersRepository.findById(toUserId);

        Follow follow = fromUser.get().follow(toUser.get());
//
//        Follow follow = new Follow();
//        follow.addFollow(fromUser.get(), toUser.get());
        followRepository.save(follow); //쿼리나감

        return true;
    }

    public boolean unFollow(Long fromUserId, Long toUserId) {
        Optional<Users> fromUser = usersRepository.findById(fromUserId);
        Optional<Users> toUser = usersRepository.findById(toUserId);

        Optional<Follow> follow = followRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);

        follow.get().unFollow();
//
//        Follow follow = new Follow();
//        follow.addFollow(fromUser.get(), toUser.get());
        followRepository.delete(follow.get()); //쿼리나감

        return true;
    }

    public List<Follow> getFollowers(Long id) {
        return followRepository.findByToUserId(id);
    }

    public List<Follow> getFollowing(Long id) {
        return followRepository.findByFromUserId(id);
    }

    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
    }
}

