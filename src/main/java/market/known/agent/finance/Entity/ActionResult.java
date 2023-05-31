package market.known.agent.finance.Entity;

import lombok.Data;
import market.known.agent.finance.Emun.ARShowType;
import market.known.agent.finance.Utils.HostIPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Objects;

@Data
public class ActionResult<T> {

    boolean success = true;
    String errorCode = "2000";
    String errorMessage = "success";

    Integer showType = ARShowType.SILENT.getType();
    String traceId = MDC.get("TRACE_ID");
    String host = HostIPHelper.IP();

    T data = null;

    public static ActionResult<?> defaultOk() {
        return new ActionResult<>();
    }

    public static <K> ActionResult<K> defaultOk(K data) {
        return new ActionResult<>(data);
    }

    public static ActionResult<?> defaultFailed(String errorCode, String errorMessage, ARShowType showType) {
        return new ActionResult<>(errorCode, errorMessage, showType);
    }

    public ActionResult(T data, String errorCode, String errorMessage, ARShowType showType) {
        this.success = Objects.equals(errorCode, "2000");
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.showType = showType.getType();
        this.data = data;

        this.recordResponse();

    }

    public ActionResult(T data) {
        this.data = data;

        this.recordResponse();
    }


    public ActionResult(String errorCode, String errorMessage, ARShowType showType) {
        this.success = Objects.equals(errorCode, "2000");
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.showType = showType.getType();

        this.recordResponse();
    }

    public ActionResult() {
        this.recordResponse();
    }

    private void recordResponse() {

        final String loggerName = "Response:%s";

        final Logger logger = LoggerFactory.getLogger(String.format(loggerName, traceId));

        final String loggerContent = "Success:%s, Code:%s, Message:%s, ShowType:%s, Data:%s";

        if (this.data != null) {
            logger.info(String.format(loggerContent,
                    this.success,
                    this.errorCode,
                    this.errorMessage,
                    this.showType,
                    this.data
            ));
        }


    }

}
