package com.amydegregorio.mappers.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amydegregorio.mappers.domain.Task;
import com.amydegregorio.mappers.dto.TaskDto;
import com.amydegregorio.mappers.repository.TaskRepository;
import com.amydegregorio.mappers.util.Priorities;
import com.amydegregorio.mappers.util.Statuses;
import com.github.dozermapper.core.Mapper;

@Controller
@RequestMapping("/task/dozer")
public class TaskDozerController {
   private final Logger LOG = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private TaskRepository taskRepository;
   @Autowired
   private Mapper dozerBeanMapper;
   
   @RequestMapping("/")
   public String listAll(Model model) {
      List<Task> tasks = taskRepository.findAll();
      
      List<TaskDto> taskDtos = tasks.stream().map(task -> dozerBeanMapper.map(task, TaskDto.class)).collect(Collectors.toList());
      model.addAttribute("tasks", taskDtos);
      return "task/dozer/list";
   }
   
   @RequestMapping(value="/add", method=RequestMethod.GET)
   public String addTask(TaskDto taskDto, Model model) {
      model.addAttribute("action", "task/dozer/add");
      return "task/dozer/entry";
   }
   
   @RequestMapping(value="/add",  params={"save"}, method=RequestMethod.POST)
   public String saveNewTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/dozer/add");
         return "task/dozer/entry";
      }
      LOG.debug(taskDto.toString());
      Task task = dozerBeanMapper.map(taskDto, Task.class);
      LOG.debug(task.toString());
      taskRepository.save(task);
      return "redirect:/task/dozer/";
   }
   
   @RequestMapping(value="/add", params={"cancel"}, method=RequestMethod.POST)
   public String cancelNewTask() {
      return "redirect:/task/dozer/";
   }
   
   @RequestMapping(value="/edit", method=RequestMethod.GET)
   public String editTask(TaskDto taskDto, Model model, @RequestParam("id") Long id) {
      model.addAttribute("action", "task/dozer/edit");
      Task task = taskRepository.getOne(id);
      taskDto = dozerBeanMapper.map(task, TaskDto.class);
      model.addAttribute("taskDto", taskDto);
      return "task/dozer/entry";
   }
   
   @RequestMapping(value="/edit",  params={"save"}, method=RequestMethod.POST)
   public String saveTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/dozer/edit");
         return "task/dozer/entry";
      }
      
      Task task = dozerBeanMapper.map(taskDto, Task.class);
      taskRepository.save(task);
      return "redirect:/task/dozer/";
   }
   
   @RequestMapping(value="/edit", params={"cancel"}, method=RequestMethod.POST)
   public String cancelTask() {
      return "redirect:/task/dozer/";
   }
   
   @RequestMapping(value="/delete", method=RequestMethod.GET) 
   public String deleteTask(@RequestParam("id") Long id){
      Task task = taskRepository.getOne(id);
      taskRepository.delete(task);
      return "redirect:/task/dozer/";
   }
   
   @ModelAttribute("priorities")
   public Priorities[] getPriorities() {
      return Priorities.values();
   }
   
   @ModelAttribute("statuses") 
   public Statuses[] getStatuses() {
      return Statuses.values();
   }

}
