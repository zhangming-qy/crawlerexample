package com.crawler.example.map;

import com.crawler.example.AppTasksStatus;
import com.crawler.example.entity.AppTasks;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper
public interface AppTasksMap {

    @Select("select id, url, group_name, order_num, jclass, status, created_time, modified_time from app_tasks where status = #{status}")
    public List<AppTasks> getAppTasksByStatus(AppTasksStatus status);

    @Update("UPDATE app_tasks SET status=#{status},modified_time=#{modified_time} WHERE id =#{id}")
    void update(AppTasks appTasks);

}

