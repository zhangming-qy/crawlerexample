package com.crawler.example.map;

import com.crawler.example.entity.ComInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ComInfoMap {

    @Insert("insert into com_info(region,category,name,description,web_url,visit_cnt,from_url,created_time, modified_time) values (#{region},#{category},#{name},#{description},#{web_url},#{visit_cnt},#{from_url},now(),now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(ComInfo comInfo);

    @Select("select region,category,name,description,web_url,visit_cnt,from_url, created_time, modified_time from com_info where id = #{id}")
    ComInfo selectById(int id);

    @Select("select region,category,name,description,web_url,visit_cnt,from_url, created_time, modified_time from com_info where web_url = #{web_url}")
    ComInfo selectByUrl(String web_url);

    @Update("update com_info set region=#{region},category=#{category},name=#{name},description=#{description},web_url=#{web_url},visit_cnt=#{visit_cnt},modified_time=now() where id=#{id}")
    void updaet(ComInfo comInfo);

    /*
    @Update("update com_info set visit_cnt=visit_cnt+1,modified_time=now() where id=#{id}")
    void updaetVisitCount(int id);

    @Update("update com_info set visit_cnt=visit_cnt+1,modified_time=now() where web_url=#{web_url}")
    void updaetVisitCount(String web_url);
    */
}
