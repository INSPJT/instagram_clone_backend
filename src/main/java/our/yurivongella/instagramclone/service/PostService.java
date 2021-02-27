package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.post.PostRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResponseDto;
import our.yurivongella.instagramclone.domain.Post.PictureURL;
import our.yurivongella.instagramclone.domain.Post.PictureURLRepository;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Post.PostRepository;
import our.yurivongella.instagramclone.domain.Users.Users;
import our.yurivongella.instagramclone.domain.Users.UsersRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostService {

    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;

    final private UsersRepository usersRepository;

    @Transactional
    public Long create(PostRequestDto postRequestDto, Users user) {

        Post post = postRequestDto.toPosts(user);
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    /*
    @Transactional
    public PostResponseDto read(Long postId) {
        Post post = postRepository.findById(postId).get();
        return PostResponseDto.toPostResponseDto(post);
    }
     */

    @Transactional
    public Long testCreate(PostRequestDto postRequestDto, Long userId) {
        Post post = postRequestDto.toPosts(usersRepository.findById(userId).get());
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostResponseDto read(Long postId, Long userId) {
        Users user = usersRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        return PostResponseDto.toPostResponseDto(post, user);

    }
}
