package us.shareby.core.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengdong
 */
//@Service("velocity")
public class ShareVelocity {


    /**
     * logger
     */
    private static final Logger logger = LoggerFactory
            .getLogger(ShareVelocity.class);


    public ShareVelocity(Map<String, Object> velocityProperties) {
        this();
        Map<String, Object> props = new HashMap<String, Object>();
        if (!velocityProperties.isEmpty()) {
            props.putAll(velocityProperties);
        }

        for (Map.Entry<String, Object> entry : props.entrySet()) {
            Velocity.setProperty(entry.getKey(), entry.getValue());
        }


    }



    public ShareVelocity() {
        Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.Log4JLogChute");

        Velocity.setProperty("runtime.log.logsystem.log4j.logger",
                logger.getName());
    }


    /**
     * merge template
     *
     * @param template
     * @param context
     * @return
     */
    public InputStream mergeTemplate(Template template, Context context) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    bos, "UTF-8"));

            template.merge(context, writer);
            writer.flush();

            ByteArrayInputStream is = new ByteArrayInputStream(
                    bos.toByteArray());

            writer.close();

            return is;

        } catch (IOException e) {

            e.printStackTrace();
            logger.error("merge template exception", e);
        } finally {

        }
        return null;
    }

    /**
     * get inputstream
     *
     * @param templatesMap
     * @param templateDir
     * @return
     */
    public InputStream getInputStreamFromTemplate(
            Map<String, Object> templatesMap, String templateDir) {
        try {
            String prefix=(String)Velocity.getProperty("class.resource.loader.path");

            Template template = Velocity.getTemplate(prefix+File.separator+templateDir, "UTF-8");
            VelocityContext context = new VelocityContext();

            for (Map.Entry<String, Object> entry : templatesMap.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
            InputStream is = this.mergeTemplate(template, context);

            return is;

        } catch (Exception e) {
            logger.error("get input stream from templateDir=" + templateDir
                    + " error", e);
        }
        return null;
    }


}
