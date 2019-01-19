package com.amydegregorio.mappers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amydegregorio.mappers.domain.Task;
import com.amydegregorio.mappers.dto.TaskDto;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
public class ApplicationConfig {

   @Bean
   public Mapper dozerBeanMapper() {
      return DozerBeanMapperBuilder.buildDefault();
   }
   
   @Bean 
   public MapperFacade orikaMapper() {
      MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
      mapperFactory.classMap(Task.class, TaskDto.class)
         .byDefault()
         .register();
      mapperFactory.classMap(TaskDto.class, Task.class)
         .byDefault()
         .register();
      
      return mapperFactory.getMapperFacade();
   }
}
