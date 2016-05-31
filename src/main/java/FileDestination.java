import org.syslog_ng.InternalMessageSender;
import org.syslog_ng.TextLogDestination;
import org.syslog_ng.options.Option;
import org.syslog_ng.options.StringOption;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileDestination extends TextLogDestination {

    private String fullPath;
    private BufferedWriter bufferedWriter = null;

    public FileDestination (long arg0){
        super(arg0);
    }

    @Override
    protected boolean send(String s) {
        try {
            this.bufferedWriter.write(s);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            return true;
        } catch (IOException e) {
            InternalMessageSender.error(e.getMessage());
        }
        return false;
    }

    @Override
    protected boolean open() {
        File file = new File(fullPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                InternalMessageSender.error(e.getMessage());
            }
        }
        if(file.exists()){
            try {
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                return true;
            } catch (IOException e) {
                InternalMessageSender.error(e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    protected void close() {
        try {
            this.bufferedWriter.close();
        } catch (IOException e) {
            InternalMessageSender.error(e.getMessage());
        }
    }

    @Override
    protected boolean isOpened() {
        if (bufferedWriter != null) {
            return true;
        }
        return false;
    }

    @Override
    protected String getNameByUniqOptions() {
        return "FileDestination("+fullPath+")";
    }

    @Override
    protected boolean init() {
        String filename;
        String filepath;
        Option fileNameOption , filePathOption;
        fileNameOption = new StringOption(this,"filename");
        filePathOption = new StringOption(this,"filepath");

        if(fileNameOption.getValue() != null) {
            filename = getOption("filename");
        } else {
            InternalMessageSender.error("File name is a required option for destination");
            return false;
        }
        if(filePathOption.getValue() != null) {
            filepath = getOption("filepath");
        } else {
            InternalMessageSender.error("File path is a required option for destination");
            return false;
        }

        fullPath = filepath + filename;
        return true;
    }

    @Override
    protected void deinit() {

    }
}
