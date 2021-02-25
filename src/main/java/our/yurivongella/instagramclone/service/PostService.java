package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.PostRequestDto;
import our.yurivongella.instagramclone.domain.Post.PictureURL;
import our.yurivongella.instagramclone.domain.Post.PictureURLRepository;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Post.PostRepository;
import our.yurivongella.instagramclone.domain.Users.Users;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostService {

    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;

    @Transactional
    public Long create(PostRequestDto postRequestDto) {
        Users users = new Users(); // mock users
        Post post = postRequestDto.toPosts(users);
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postRequestDto.toPictureURLs());
        post.getPictureURLs().addAll(list);

        return post.getId();
    }
}
