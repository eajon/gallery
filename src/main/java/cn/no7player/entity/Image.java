package cn.no7player.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "related_type")
    private Integer relatedType;

    private String url;

    @Column(name = "url_key")
    private String urlKey;

    private String style;

    private String remark;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return related_id
     */
    public Long getRelatedId() {
        return relatedId;
    }

    /**
     * @param relatedId
     */
    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    /**
     * @return related_type
     */
    public Integer getRelatedType() {
        return relatedType;
    }

    /**
     * @param relatedType
     */
    public void setRelatedType(Integer relatedType) {
        this.relatedType = relatedType;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * @return url_key
     */
    public String getUrlKey() {
        return urlKey;
    }

    /**
     * @param urlKey
     */
    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey == null ? null : urlKey.trim();
    }

    /**
     * @return style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     */
    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return created_at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return updated_at
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}