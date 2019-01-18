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
import com.amydegregorio.mappers.dto.TaskMapper;
import com.amydegregorio.mappers.repository.TaskRepository;
import com.amydegregorio.mappers.util.Priorities;
import com.amydegregorio.mappers.util.Statuses;

@Controller
@RequestMapping("/task/mapstruct")
public class TaskMapStructController {
private final Logger LOG = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private TaskRepository taskRepository;
   
   @RequestMapping("/")
   public String listAll(Model model) {
      List<Task> tasks = taskRepository.findAll();
      
      List<TaskDto> taskDtos = tasks.stream().map(task -> TaskMapper.INSTANCE.outgoing(task)).collect(Collectors.toList());
      model.addAttribute("tasks", taskDtos);
      return "task/mapstruct/list";
   }
   
   @RequestMapping(value="/add", method=RequestMethod.GET)
   public String addTask(TaskDto taskDto, Model model) {
      model.addAttribute("action", "task/mapstruct/add");
      return "task/mapstruct/entry";
   }
   
   @RequestMapping(value="/add",  params={"save"}, method=RequestMethod.POST)
   public String saveNewTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/mapstruct/add");
         return "task/mapstruct/entry";
      }
      LOG.debug(taskDto.toString());
      Task task = TaskMapper.INSTANCE.incoming(taskDto);
      LOG.debug(task.toString());
      taskRepository.save(task);
      return "redirect:/task/mapstruct/";
   }
   
   @RequestMapping(value="/add", params={"cancel"}, method=RequestMethod.POST)
   public String cancelNewTask() {
      return "redirect:/task/mapstruct/";
   }
   
   @RequestMapping(value="/edit", method=RequestMethod.GET)
   public String editTask(TaskDto taskDto, Model model, @RequestParam("id") Long id) {
      model.addAttribute("action", "task/mapstruct/edit");
      Task task = taskRepository.getOne(id);
      taskDto = TaskMapper.INSTANCE.outgoing(task);
      model.addAttribute("taskDto", taskDto);
      return "task/mapstruct/entry";
   }
   
   @RequestMapping(value="/edit",  params={"save"}, method=RequestMethod.POST)
   public String saveTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/mapstruct/edit");
         return "task/mapstruct/entry";
      }
      
      Task task = TaskMapper.INSTANCE.incoming(taskDto);
      taskRepository.save(task);
      return "redirect:/task/mapstruct/";
   }
   
   @RequestMapping(value="/edit", params={"cancel"}, method=RequestMethod.POST)
   public String cancelTask() {
      return "redirect:/task/mapstruct/";
   }
   
   @RequestMapping(value="/delete", method=RequestMethod.GET) 
   public String deleteTask(@RequestParam("id") Long id){
      Task task = taskRepository.getOne(id);
      taskRepository.delete(task);
      return "redirect:/task/mapstruct/";
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
