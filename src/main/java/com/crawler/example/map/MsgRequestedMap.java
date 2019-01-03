package com.crawler.example.map;

import com.crawler.example.entity.MsgRequested;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MsgRequestedMap {

    @Insert("insert into msg_requested(com_info_id,created_time, modified_time) values (#{com_info_id},now(),now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(MsgRequested msgRequested);


}
