package cn.no7player.service;

import cn.no7player.controller.pagination.PageMeta;
import cn.no7player.controller.pagination.PageQuery;
import cn.no7player.entity.Image;
import cn.no7player.entity.User;
import cn.no7player.mapper.generator.ImageMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class ImageService {

    @Autowired
    ImageMapper imageMapper;


    @PageQuery
    public PageMeta getImages(User user,Long folderId)
    {
        Example example=new Example(Image.class);
        example.createCriteria().andEqualTo("userId",user.getId()).andEqualTo("relatedId",folderId).andEqualTo("relatedType",1);
        example.setOrderByClause("id Desc");
        PageInfo pageInfo =new PageInfo<>(imageMapper.selectByExample(example));
        return new PageMeta(pageInfo);
    }


    public int addImage(Image image)
    {
       return imageMapper.insertSelective(image);
    }

}
