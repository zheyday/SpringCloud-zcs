package mmm.goshop.model;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author zcs
 * @since 2021-04-20
 */
@ApiModel(value="Index_category对象", description="")
public class IndexCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Boolean is_in_serving;

    private String description;

    private String title;

    private String link;

    private String image_url;

    private String icon_url;

    private String title_color;

    private Integer __v;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getIs_in_serving() {
        return is_in_serving;
    }

    public void setIs_in_serving(Boolean is_in_serving) {
        this.is_in_serving = is_in_serving;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
    public String getTitle_color() {
        return title_color;
    }

    public void setTitle_color(String title_color) {
        this.title_color = title_color;
    }
    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    @Override
    public String toString() {
        return "Index_category{" +
            "id=" + id +
            ", is_in_serving=" + is_in_serving +
            ", description=" + description +
            ", title=" + title +
            ", link=" + link +
            ", image_url=" + image_url +
            ", icon_url=" + icon_url +
            ", title_color=" + title_color +
            ", __v=" + __v +
        "}";
    }
}
