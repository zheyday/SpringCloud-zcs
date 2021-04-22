package mmm.goshop.controller;


import com.google.gson.*;
import mmm.goshop.model.IndexCategory;
import mmm.goshop.service.impl.IndexCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zcs
 * @since 2021-04-19
 */
@RestController
public class IndexCategoryController {
    private final IndexCategoryServiceImpl categoryService;
    private final Gson gson = new Gson();

    @Autowired
    public IndexCategoryController(IndexCategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/init")
    public void init(){
        String json="";
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            IndexCategory category = gson.fromJson(jsonElement, IndexCategory.class);
            categoryService.save(category);
        }
    }
    @GetMapping("/index_category")
    public String getCategory(){
        return gson.toJson(categoryService.list());
    }
}
