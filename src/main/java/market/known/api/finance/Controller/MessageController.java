package market.known.api.finance.Controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import market.known.api.finance.Entity.ActionResult;
import market.known.api.finance.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/messages/{seq}")
    public ActionResult<List<JSONObject>> getMessages(@RequestAttribute String body, @PathVariable Integer seq) {

        JSONObject object = JSONUtil.parseObj(body);

        try {
            messageService.init(
                    object.get("corpId", String.class),
                    object.get("financeSecret", String.class),
                    object.get("financePrivateKey", String.class)
            );

            List<JSONObject> messagesList = messageService.getMessages(seq, 50, "", "", 5000);

            return ActionResult.defaultOk(messagesList);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/media/{id}")
    public ActionResult<?> getMedia(@RequestAttribute String body, @PathVariable String id) {

        JSONObject object = JSONUtil.parseObj(body);

        try {
            messageService.init(
                    object.get("corpId", String.class),
                    object.get("financeSecret", String.class),
                    object.get("financePrivateKey", String.class)
            );
            String filename = messageService.getMediaData(id);
            return ActionResult.defaultOk(filename);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
