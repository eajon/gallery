package cn.no7player.controller;

import cn.no7player.controller.pagination.PageMeta;
import cn.no7player.entity.Image;
import cn.no7player.entity.User;
import cn.no7player.exception.BusinessException;
import cn.no7player.response.ResponseBean;
import cn.no7player.service.OssService;
import cn.no7player.service.ImageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    OssService ossService;

    @Value("${gallery.file.stylename}")
    private String STYLE_NAME;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    public ResponseBean getImage(Long folderId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        PageMeta page = imageService.getImages(user, folderId);
        if (page.getList().size() > 0) {
            return new ResponseBean(0, "成功", page);
        } else {
            throw new BusinessException("无数据");
        }

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean upload(MultipartFile[] files,String folder,String host, @RequestParam Long folderId, String remark) throws IOException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, String> map = ossService.upload(files, folder, host);
        for (String key : map.keySet()) {
            Image image = new Image();
            image.setUserId(user.getId());
            image.setRelatedId(folderId);
            image.setRelatedType(1);
            image.setUrlKey(key);
            image.setUrl(map.get(key));
            image.setStyle(STYLE_NAME);
            image.setRemark(remark);
            imageService.addImage(image);
        }
        return new ResponseBean(map);
    }

}
