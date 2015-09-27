package io.feedback.survey.service;

import java.util.List;

import io.feedback.survey.entity.Project;
import io.feedback.survey.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    
    public void addProject(Project project) {
        getProjectRepository().insert(project);
    }
    
    public List<Project> findAllProjects() {
        return getProjectRepository().findAll();
    }
}