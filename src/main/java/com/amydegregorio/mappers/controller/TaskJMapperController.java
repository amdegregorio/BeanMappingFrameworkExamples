package com.amydegregorio.mappers.controller;

import static com.googlecode.jmapper.api.JMapperAPI.conversion;
import static com.googlecode.jmapper.api.JMapperAPI.global;
import static com.googlecode.jmapper.api.JMapperAPI.mappedClass;

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
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.googlecode.jmapper.api.JMapperAPI;

@Controller
@RequestMapping("/task/jmapper")
public class TaskJMapperController {
   private final Logger LOG = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private TaskRepository taskRepository;
   
   private JMapper<TaskDto, Task> outgoingMapper;
   private JMapper<Task, TaskDto> incomingMapper;
   
   public TaskJMapperController() {
      JMapperAPI outgoingAPI = new JMapperAPI();
      outgoingAPI.add(mappedClass(TaskDto.class).add(global())
               .add(conversion("priority")
                  .from("priority").to("priority")
                  .type(JMapConversion.Type.DYNAMIC)
                  .body("if (${source} == com.amydegregorio.mappers.util.Priorities.HIGH) {"
                      + "return \"HIGH\"; } "
                      + "else if (${source} == com.amydegregorio.mappers.util.Priorities.MEDIUM) {"
                      + "return \"MEDIUM\"; }"
                      + "else if (${source} == com.amydegregorio.mappers.util.Priorities.LOW) {"
                      + "return \"LOW\"; }"
                      + "else if (${source} == com.amydegregorio.mappers.util.Priorities.URGENT) {"
                      + "return \"URGENT\"; }"
                      +"else return \"\";"))
                .add(conversion("status")
                   .from("status").to("status")
                   .type(JMapConversion.Type.DYNAMIC)
                   .body("if (${source} == com.amydegregorio.mappers.util.Statuses.NOT_STARTED) {" 
                      + "return \"NOT_STARTED\";}"
                      + "else if (${source} == com.amydegregorio.mappers.util.Statuses.IN_PROGRESS) { "
                      + "return \"IN_PROGRESS\";}"
                      + "else if (${source} == com.amydegregorio.mappers.util.Statuses.COMPLETE) {"
                      + "return \"COMPLETE\";} "
                      + "return \"\";"))
         );
      
      JMapperAPI incomingAPI = new JMapperAPI();
      incomingAPI.add(mappedClass(Task.class).add(global())
            .add(conversion("priority")
            .from("priority").to("priority")
            .type(JMapConversion.Type.DYNAMIC)
            .body("if (${source}.equals(\"HIGH\")) {" 
               + "return com.amydegregorio.mappers.util.Priorities.HIGH;}"
               + "else if(${source}.equals(\"MEDIUM\")) {"
               + "return com.amydegregorio.mappers.util.Priorities.MEDIUM;}"
               + "else if(${source}.equals(\"LOW\")) {"
               + "return com.amydegregorio.mappers.util.Priorities.LOW;}"
               + "else if(${source}.equals(\"URGENT\")) {"
               + "return com.amydegregorio.mappers.util.Priorities.URGENT;}"
               + "else { return null;}"))
            .add(conversion("status")
            .from("status").to("status")
            .type(JMapConversion.Type.DYNAMIC)
            .body("if (${source}.equals(\"NOT_STARTED\")) {"
               + "return com.amydegregorio.mappers.util.Statuses.NOT_STARTED;}"
               + "else if(${source}.equals(\"IN_PROGRESS\")) {"
               + "return com.amydegregorio.mappers.util.Statuses.IN_PROGRESS;}"
               + "else if(${source}.equals(\"COMPLETE\")) {"
               + "return com.amydegregorio.mappers.util.Statuses.COMPLETE;}"
               + "else { return null;}"))
         );
      
      outgoingMapper = new JMapper<>(TaskDto.class, Task.class, outgoingAPI);
      incomingMapper = new JMapper<>(Task.class, TaskDto.class, incomingAPI);
   }
   
   @RequestMapping("/")
   public String listAll(Model model) {
      List<Task> tasks = taskRepository.findAll();
      
      List<TaskDto> taskDtos = tasks.stream().map(task -> outgoingMapper.getDestination(task)).collect(Collectors.toList());
      model.addAttribute("tasks", taskDtos);
      return "task/jmapper/list";
   }
   
   @RequestMapping(value="/add", method=RequestMethod.GET)
   public String addTask(TaskDto taskDto, Model model) {
      model.addAttribute("action", "task/jmapper/add");
      return "task/jmapper/entry";
   }
   
   @RequestMapping(value="/add",  params={"save"}, method=RequestMethod.POST)
   public String saveNewTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/jmapper/add");
         return "task/jmapper/entry";
      }
      LOG.debug(taskDto.toString());
      Task task = incomingMapper.getDestination(taskDto);
      LOG.debug(task.toString());
      taskRepository.save(task);
      return "redirect:/task/jmapper/";
   }
   
   @RequestMapping(value="/add", params={"cancel"}, method=RequestMethod.POST)
   public String cancelNewTask() {
      return "redirect:/task/jmapper/";
   }
   
   @RequestMapping(value="/edit", method=RequestMethod.GET)
   public String editTask(TaskDto taskDto, Model model, @RequestParam("id") Long id) {
      model.addAttribute("action", "task/jmapper/edit");
      Task task = taskRepository.getOne(id);
      taskDto = outgoingMapper.getDestination(task);
      model.addAttribute("taskDto", taskDto);
      return "task/jmapper/entry";
   }
   
   @RequestMapping(value="/edit",  params={"save"}, method=RequestMethod.POST)
   public String saveTask(@Valid TaskDto taskDto, BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
         model.addAttribute("action", "task/jmapper/edit");
         return "task/jmapper/entry";
      }
      
      Task task = incomingMapper.getDestination(taskDto);
      taskRepository.save(task);
      return "redirect:/task/jmapper/";
   }
   
   @RequestMapping(value="/edit", params={"cancel"}, method=RequestMethod.POST)
   public String cancelTask() {
      return "redirect:/task/jmapper/";
   }
   
   @RequestMapping(value="/delete", method=RequestMethod.GET) 
   public String deleteTask(@RequestParam("id") Long id){
      Task task = taskRepository.getOne(id);
      taskRepository.delete(task);
      return "redirect:/task/jmapper/";
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
