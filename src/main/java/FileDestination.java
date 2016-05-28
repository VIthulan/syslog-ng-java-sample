import org.syslog_ng.InternalMessageSender;
import org.syslog_ng.TextLogDestination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileDestination extends TextLogDestination {

    private String filename;
    private String filepath;
    private BufferedWriter bufferedWriter;

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
        return true;
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
        return true;
    }

    @Override
    protected String getNameByUniqOptions() {
        return "JavaDestToFile";
    }

    @Override
    protected boolean init() {
        this.filename = getOption("filename");
        this.filepath = getOption("filepath");

        if (filename == null) {
            InternalMessageSender.error("File name is a required option for destination");
            return false;
        }
        if (filepath == null) {
            InternalMessageSender.error("File path is a required option for destination");
            return false;
        }
        File file = new File(filepath+filename);
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
        InternalMessageSender.error("Something went wrong at "+filepath+filename);
        return false;
    }

    @Override
    protected void deinit() {

    }
}
