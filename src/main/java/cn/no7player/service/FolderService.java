package cn.no7player.service;


import cn.no7player.controller.pagination.PageMeta;
import cn.no7player.controller.pagination.PageQuery;
import cn.no7player.entity.Folder;
import cn.no7player.entity.User;
import cn.no7player.mapper.generator.FolderMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {


    @Autowired
    FolderMapper folderMapper;

    public int addFolder(String name, User user) {
        Folder folder = new Folder();
        folder.setUserId(user.getId());
        folder.setFoldersName(name);
        return folderMapper.insertSelective(folder);
    }

    @PageQuery
    public PageMeta getFolders(User user) {
        Folder folder = new Folder();
        folder.setUserId(user.getId());
        PageInfo pageInfo =new PageInfo<>(folderMapper.select(folder));
        return new PageMeta(pageInfo) ;
    }
}
