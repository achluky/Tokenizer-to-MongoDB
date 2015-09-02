package utils;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

import main.Configuration;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AccountUtils.class);
	private static ArrayList<String> arr_account   	= new ArrayList<String>();    
    private static final AccountUtils INSTANCE 		= new AccountUtils();

    public AccountUtils(){
        AccountNews_(Configuration.getAccount());
    }
    
    public AccountUtils(String pathFileAcNews){
        AccountNews_(pathFileAcNews);
    }
    
    public static AccountUtils getInstance(){
        return INSTANCE;
    }
    
    private void AccountNews_(String ACNEWS_FILE_NAME) {
        FillAcNewsArrayList(new File(ACNEWS_FILE_NAME));
    }

    private void FillAcNewsArrayList(File file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String Account = "";
            while ((Account = br.readLine()) != null) {
                arr_account.add(Account);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
   public static boolean SearchScreenName(String ScNametweet){
        if (arr_account.contains(ScNametweet)) {
            return true;
        }else{
            return false;
        }
    }
   
   
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
        String screen_name = "detikcom";
        AccountUtils accountUtils = AccountUtils.getInstance();
        if(accountUtils.SearchScreenName(screen_name))
        {
            LOG.info("Account "+screen_name+" Found");
        }else{
        	LOG.info("Account "+screen_name+" Not Found");
        }
    }
}
