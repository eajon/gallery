package cn.no7player.controller;

import cn.no7player.controller.pagination.PageMeta;
import cn.no7player.entity.Folder;
import cn.no7player.entity.User;
import cn.no7player.exception.BusinessException;
import cn.no7player.response.ResponseBean;
import cn.no7player.service.FolderService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/folder")
public class FolderController {


    @Autowired
    FolderService folderService;

    @PostMapping("/add")
    @ResponseBody
    @RequiresAuthentication
    public ResponseBean add(@RequestParam("folderName") String folderName) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        int returnCode = folderService.addFolder(folderName, user);
        if (returnCode > 0) {
            return new ResponseBean(0, "成功");
        } else {
            throw new BusinessException("失败");
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    public ResponseBean getFolder() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        PageMeta page =folderService.getFolders(user);
        if(page.getList().size()>0)
        {
            return new ResponseBean(0, "成功",  page);
        }else
        {
            throw new BusinessException("无数据");
        }

    }
}
