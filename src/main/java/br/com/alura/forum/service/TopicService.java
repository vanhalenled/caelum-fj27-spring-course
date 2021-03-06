package br.com.alura.forum.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.alura.forum.dto.input.NewTopicInputDto;
import br.com.alura.forum.dto.output.TopicOutputDto;
import br.com.alura.forum.exception.ResourceNotFoundException;
import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;

@Service
public class TopicService {

	private TopicRepository topicRepository;
	private CourseRepository courseRepository;

	public TopicService(TopicRepository topicRepository, CourseRepository courseRepository) {
		this.topicRepository = topicRepository;
		this.courseRepository = courseRepository;
	}

	public Page<Topic> findAll(Specification<Topic> topicSearchSpecification, Pageable pageable) {
		return topicRepository.findAll(topicSearchSpecification, pageable);
	}

	public TopicOutputDto createTopic(NewTopicInputDto newTopicDto, User user) {
		Course course = courseRepository
				.findByName(newTopicDto.getCourseName())
				.orElseThrow(ResourceNotFoundException::new);
		Topic topic = new Topic(
				newTopicDto.getShortDescription(), 
				newTopicDto.getContent(), 
				user, 
				course);
		return new TopicOutputDto(topicRepository.save(topic));
	}

	public List<Topic> findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(User user, Instant instant) {
		return topicRepository.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(user, instant);
	}

	public TopicOutputDto findById(Long id) {
		return new TopicOutputDto(
				topicRepository
					.findById(id)
					.orElseThrow(ResourceNotFoundException::new));
	}
	
}
