package BackEndPackage;

import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import CompanyPackage.Operator;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import Utilities.DateConverter;
import Utilities.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

public class QCStructure
{
    private static QCStructure structure = new QCStructure();
    private QCStructure(){}
    public static QCStructure getStructure(){ return structure;}

    private Utils utils = new Utils();

    private String LOGIN_URL = "https://pixelqc.ir/api-token-auth/";
    private String HC_URL = "https://pixelqc.ir/healthcenter/api/get/";
    private String DEVICE_URL = "https://pixelqc.ir/devices/api/device/get/";
    private String ATTRIBUTE_URL = "https://pixelqc.ir/devices/api/att/get/";
    private String COMPANY_INFO_URL  = "https://pixelqc.ir/accounts/api/company/info/";
    private String OPERATOR_INFO_URL  = "https://pixelqc.ir/accounts/api/operator/info/";
    private String COMPANY_DEVICES_URL  = "https://pixelqc.ir/devices/api/category/devices/";
    private String NEWS_URL  = "https://pixelqc.ir/accounts/api/news/";


    private String token;
    private int company_id = 4;
    private int mainHC_id;
    private int mainDevice_id;

    public boolean setToken(String username , String password) throws FileNotFoundException {
        JSONObject logInObj = null;

        try {
            String result = sendLogInPOST(LOGIN_URL , username , password);
            JSONParser parser = new JSONParser();

            if (result == null || result.equalsIgnoreCase(""))
                return false;

            logInObj = (JSONObject) parser.parse(result);

            if (logInObj.get("token") == null) {
                System.out.println("Result1: false");
                return false;
            }

            this.token = logInObj.get("token").toString();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("Result2: false");
            return false;
        }

        return true;
    }

    private static String sendLogInPOST(String url , String username , String password) throws IOException {

        String result = "";
        HttpPost post = new HttpPost(url);

        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
//        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)){

            result = EntityUtils.toString(response.getEntity());
        }
        catch (UnknownHostException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }
        return result;
    }

    public void make_map() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try
        {
            HttpGet request = new HttpGet(COMPANY_INFO_URL);
            request.addHeader("Authorization","Token " + this.token);

            CloseableHttpResponse response = httpClient.execute(request);

            try
            {
                Company company = Company.getCompany();
                Operator operator = Operator.getOperator();

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                    if(result.equalsIgnoreCase("[]"))
                    {
                        initOperator();
                        return;
                    }
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray array = null;

                array = (JSONArray) parser.parse(result);

                for (Object object : array)
                {
                    JSONObject jsonObject = (JSONObject) object;

                    company.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    company.setName((String) jsonObject.get("name"));
                    company.setEmail((String) jsonObject.get("email"));
                    company.setPhoneNo((String) jsonObject.get("phone_number"));
                    company.setCategory((String) jsonObject.get("category"));
                    company.setAddress((String) jsonObject.get("address"));
                    company.setAccess(true);

                    operator.setFirst_name((String) jsonObject.get("first_name"));
                    operator.setLast_name((String) jsonObject.get("last_name"));
                    operator.setUser_id((Integer.parseInt(jsonObject.get("company_user").toString())));
                }

                setCenters();
                setCompanyDevices();
            }
            catch (ParseException e) {
                Utils utils = new Utils();
                System.out.println("salam 2");
                utils.make_WarningAlert("there is a problem with server!");
                return;
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            System.out.println("salam 3");
            utils.make_WarningAlert("there is a problem with server!");
            return;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            System.out.println("salam 4");
            utils.make_WarningAlert("there is a problem with server!");
            return;
        } finally {
            httpClient.close();
        }

    }

    private void initOperator() throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try
        {
            HttpGet request = new HttpGet(OPERATOR_INFO_URL);
            request.addHeader("Authorization","Token " + this.token);

            CloseableHttpResponse response = httpClient.execute(request);

            try
            {
                Operator operator = Operator.getOperator();
                Company company = Company.getCompany();

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray array = null;

                array = (JSONArray) parser.parse(result);

                for (Object object : array)
                {
                    JSONObject jsonObject = (JSONObject) object;

                    operator.setFirst_name((String) jsonObject.get("first_name"));
                    operator.setLast_name((String) jsonObject.get("last_name"));
                    operator.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    operator.setUser_id((Integer.parseInt(jsonObject.get("user").toString())));

                    company.setName((String) jsonObject.get("company_name"));
                    company.setPhoneNo((String) jsonObject.get("phone_number"));
                    company.setAddress((String) jsonObject.get("address"));
                    company.setId(Integer.parseInt(jsonObject.get("company").toString()));
                    company.setAccess(false);
                }

                setCenters();
                setCompanyDevices();
            }
            catch (ParseException e) {
                Utils utils = new Utils();
                System.out.println("salam 5");
                utils.make_WarningAlert("there is a problem with server!");
                return;
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            System.out.println("salam 6");
            utils.make_WarningAlert("there is a problem with server!");
            return;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            System.out.println("salam 7");
            utils.make_WarningAlert("there is a problem with server!");
            return;
        } finally {
            httpClient.close();
        }
    }

    private void setCompanyDevices() throws IOException {
        List<Device> inputDevices = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(COMPANY_DEVICES_URL);

            // add request headers
            request.addHeader("Authorization","Token " + this.token);

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray deviceArray = (JSONArray) parser.parse(result);

                for(Object tempObj : deviceArray)
                {
                    JSONObject jsonObject = (JSONObject) tempObj;

                    Device device = new Device(0);
                    int deviceId = Integer.parseInt(jsonObject.get("id").toString());
                    device.setId(deviceId);
                    device.setPerNameOfDevice(jsonObject.get("persian_name").toString());
                    device.setEngNameOfDevice(jsonObject.get("english_name").toString());
                    device.setIsoNo(jsonObject.get("iso_number").toString());
                    device.setCategories(setCategories(jsonObject.get("category").toString()));

                    setDeviceAttributes(deviceId , device);

                    inputDevices.add(device);
                }

                Company.getCompany().setDevices(inputDevices);

                /////////////////////////////////////////////////


            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

    }

    private Object[] setCategories(String categories) {
        return (categories.split(";"));
    }

    private Object[] setChoices(String choices) {
        return (choices.split(";"));
    }

    private String blockHC_IP(String ip, String token, HealthCenter center) throws IOException {

        String result = "";

        HttpPost post = new HttpPost(HC_URL);
        post.addHeader("Authorization", "Token " + token);
        post.setHeader("Content-type", "application/json");

        JSONObject temp = new JSONObject();
        temp.put("name" , center.getName());
        temp.put("address" , center.getAddress());
        temp.put("city" , center.getCity());
        temp.put("serial" , center.getSerial());
//        temp.put("company" , this.company_id);
        temp.put("operator" , Operator.getOperator().getId());

        // send a JSON data
        post.setEntity(new StringEntity(temp.toString(), "UTF-8"));

        System.out.println(post.toString());

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }

        return result;
    }

    public boolean setNewHC(HealthCenter center)
    {
        JSONObject tempObj = null;
        JSONParser parser = new JSONParser();

        try
        {
            String result = blockHC_IP("1.1.1.1" , this.token , center);
            tempObj = (JSONObject) parser.parse(result);
            if(tempObj.get("id") == null)
                return false;
            this.mainHC_id = Integer.parseInt(tempObj.get("id").toString());
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String blockDevice_IP(String ip, String token, Device device) throws IOException {

        String result = "";

        HttpPost post = new HttpPost(DEVICE_URL);
        post.addHeader("Authorization", "Token " + token);
        post.setHeader("Content-type", "application/json");

        JSONObject temp = new JSONObject();
        temp.put("persian_name" , device.getPerNameOfDevice());
        temp.put("english_name" , device.getEngNameOfDevice());
        temp.put("serial_number" , device.getSerialNo());
        temp.put("iso_number" , device.getIsoNo());
        temp.put("result" , device.getResult());
        temp.put("company" , Company.getCompany().getId());
        temp.put("owner" , this.mainHC_id);

        // send a JSON data
        post.setEntity(new StringEntity(temp.toString(), "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }

        catch (UnknownHostException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }

        return result;
    }

    public boolean setNewDevice(Device device)
    {
        JSONObject tempObj = null;
        JSONParser parser = new JSONParser();

        try
        {
            String result = blockDevice_IP("1.1.1.1" , this.token , device);

            tempObj = (JSONObject) parser.parse(result);
            this.mainDevice_id =  Integer.parseInt(tempObj.get("id").toString());
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private String blockCategoryDevice_IP(String ip, String token, Device device) throws IOException {

        String result = "";

        HttpPost post = new HttpPost(DEVICE_URL);
        post.addHeader("Authorization", "Token " + token);
        post.setHeader("Content-type", "application/json");

        JSONObject temp = new JSONObject();
        temp.put("persian_name" , device.getPerNameOfDevice());
        temp.put("english_name" , device.getEngNameOfDevice());
        temp.put("serial_number" , "testDevice");
        temp.put("iso_number" , device.getIsoNo());
        temp.put("category" , getDeviceCategories(device));
        temp.put("result" , "pass");
        temp.put("company" , Company.getCompany().getId());
//        temp.put("owner" , this.mainHC_id);

        // send a JSON data
        post.setEntity(new StringEntity(temp.toString(), "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }

        catch (UnknownHostException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }

        return result;
    }

    private String getDeviceCategories(Device device)
    {
        String categories = "";

        for (String category : device.getCategories())
        {
            categories += (category + ";");
        }

        return categories;
    }

    public boolean setNewCategoryDevice(Device device)
    {
        JSONObject tempObj = null;
        JSONParser parser = new JSONParser();

        try
        {
            String result = blockCategoryDevice_IP("1.1.1.1" , this.token , device);

            tempObj = (JSONObject) parser.parse(result);
            this.mainDevice_id =  Integer.parseInt(tempObj.get("id").toString());
        }
        catch (IOException | ParseException e)
        {
            System.out.println("salam3");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String blockAttribute_IP(String ip, String token, Attribute attribute) throws IOException {

        String result = "";

        HttpPost post = new HttpPost(ATTRIBUTE_URL);
        post.addHeader("Authorization", "Token " + token);
        post.setHeader("Content-type", "application/json");

        JSONObject temp = new JSONObject();

        if (attribute.getMainValue() == null || attribute.getMainValue().equalsIgnoreCase(""))
        {
            attribute.setMainValue("N/A");
        }

        String choices = "";

        if (attribute.getChoicesNames() == null || attribute.getChoicesNames().isEmpty())
        {
            choices = "N/A";
        }
        else
        {
            choices = getAttributeChoices(attribute);
        }

        temp.put("name" , attribute.getName());
        temp.put("value" , attribute.getMainValue());
        temp.put("mode" , attribute.getMode());
        temp.put("max_value" , attribute.getMax());
        temp.put("min_value" , attribute.getMin());
        temp.put("category" , attribute.getCategory());
        temp.put("choices" , choices);
        temp.put("automatic_fill" , attribute.isAutomaticFill());
        temp.put("owner" , this.mainDevice_id);

        // send a JSON data
        post.setEntity(new StringEntity(temp.toString(), "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }
        catch (UnknownHostException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }

        return result;
    }

    private String getAttributeChoices(Attribute attribute)
    {
        String choices = null;

        for (Object choice : attribute.getChoicesNames())
        {
            choices += (choice.toString() + ";");
        }

        return choices;
    }

    public boolean setNewAttribute(Attribute attribute)
    {
        JSONObject tempObj = null;
        JSONParser parser = new JSONParser();

        try
        {
            String result = blockAttribute_IP("1.1.1.1" , this.token , attribute);
            tempObj = (JSONObject) parser.parse(result);
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean setAttributes(Device device)
    {
        boolean result = true;

        for (Attribute attribute : device.getAllAttributes())
        {
            result = setNewAttribute(attribute);
        }

        return result;
    }

    public void setCenters() throws IOException
    {
        List<HealthCenter> centers = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(HC_URL);

            // add request headers
            request.addHeader("Authorization","Token " + this.token);

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray array = null;

                array = (JSONArray) parser.parse(result);

                for (Object object : array)
                {
                    JSONObject jsonObject = (JSONObject) object;

                    HealthCenter healthCenter = new HealthCenter();
                    healthCenter.id = Integer.parseInt(jsonObject.get("id").toString());
                    healthCenter.setSerial((String) jsonObject.get("serial"));
                    healthCenter.setName((String) jsonObject.get("name"));
                    healthCenter.setAddress((String) jsonObject.get("address"));
                    healthCenter.setCity((String) jsonObject.get("city"));
                    healthCenter.setDate(DateConverter.getDate(jsonObject.get("date").toString().substring(0,11)));

                    centers.add(healthCenter);
                }

                /////////////////////////////////////////////////

            }
            catch (ParseException e) {
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return ;
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return ;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return ;
        } finally {
            httpClient.close();
        }

        Company.getCompany().setHealthCenters(centers);
    }


    public void setDevices() throws IOException
    {
        List<Device> devices = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(DEVICE_URL);

            // add request headers
            request.addHeader("Authorization","Token " + this.token);

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return ;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray array = null;

                array = (JSONArray) parser.parse(result);

                for (Object object : array)
                {
                    JSONObject jsonObject = (JSONObject) object;

                    if(jsonObject.get("owner").toString() == null)
                    {
                        continue;
                    }

                    Device device = new Device(0);
                    int deviceId = Integer.parseInt(jsonObject.get("id").toString());

                    device.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    device.setOwner(Integer.parseInt(jsonObject.get("owner").toString()));
                    device.setPerNameOfDevice(jsonObject.get("persian_name").toString());
                    device.setEngNameOfDevice(jsonObject.get("english_name").toString());
                    device.setSerialNo(jsonObject.get("serial_number").toString());
                    device.setIsoNo(jsonObject.get("iso_number").toString());
                    device.setResult(jsonObject.get("result").toString());

                    setDeviceAttributes(deviceId , device);

                    devices.add(device);
                }

            }
            catch (ParseException e) {
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return ;
            } finally {
                response.close();
            }
        } catch (IOException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return ;
        } finally {
            httpClient.close();
        }

        Company.getCompany().setSubmittedDevices(devices);
    }


    public void setInputDevices(int id) throws IOException
    {
        List<Device> inputDevices = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(HC_URL + "pro/" + id + "/");

            // add request headers
            request.addHeader("Authorization","Token " + this.token);
//            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return ;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONObject hcObject = null;

                hcObject = (JSONObject) parser.parse(result);

                JSONArray deviceArray = null;
                deviceArray = (JSONArray) hcObject.get("Device");

                for(Object tempObj : deviceArray)
                {
                    JSONObject jsonObject = (JSONObject) tempObj;

                    Device device = new Device(0);
                    int deviceId = Integer.parseInt(jsonObject.get("id").toString());

                    device.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    device.setPerNameOfDevice(jsonObject.get("persian_name").toString());
                    device.setEngNameOfDevice(jsonObject.get("english_name").toString());
                    device.setSerialNo(jsonObject.get("serial_number").toString());
                    device.setIsoNo(jsonObject.get("iso_number").toString());
                    device.setResult(jsonObject.get("result").toString());

                    setDeviceAttributes(deviceId , device);

                    inputDevices.add(device);
                }

                Company.getCompany().getMainHealthCenter().setInputDevices(inputDevices);

                /////////////////////////////////////////////////


            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

    }


    public void setDeviceAttributes(int id , Device device) throws IOException
    {
        List<Attribute> addedAttributeList = new ArrayList<>();
        List<Attribute> defaultAttributeList = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(DEVICE_URL + id + "/");

            // add request headers
            request.addHeader("Authorization","Token " + this.token);
//            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                String result = null;

                if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
                {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return ;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONObject deviceObject = null;

                deviceObject = (JSONObject) parser.parse(result);

                JSONArray deviceArray = null;
                deviceArray = (JSONArray) deviceObject.get("attribute");

                for(Object tempObj : deviceArray)
                {
                    JSONObject jsonObject = (JSONObject) tempObj;

                    Attribute attribute = new Attribute();

                    attribute.setName(jsonObject.get("name").toString());
                    attribute.setMainValue(jsonObject.get("value").toString());
                    attribute.setMode(jsonObject.get("mode").toString());
                    attribute.setCategory(jsonObject.get("category").toString());
                    attribute.setMax(Double.parseDouble(jsonObject.get("max_value").toString()));
                    attribute.setMin(Double.parseDouble(jsonObject.get("min_value").toString()));
                    attribute.setAutomaticFill(Boolean.parseBoolean(jsonObject.get("automatic_fill").toString()));
                    attribute.setChoices(setChoices(jsonObject.get("choices").toString()));

                    if (attribute.getCategory().equalsIgnoreCase("default"))
                    {
                        defaultAttributeList.add(attribute);
                    }
                    else
                    {
                        addedAttributeList.add(attribute);
                    }
                }

                device.setDefaultAttributes(defaultAttributeList);
                device.setAllAttributes(addedAttributeList);

                /////////////////////////////////////////////////


            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

    }

    public void setCategoryDevices(List<Device> deviceList , HealthCenter healthCenter)
    {
        JSONObject tempMap = new JSONObject();

        for (Device device : deviceList)
        {
            Device tempDevice = new Device(device);
            tempDevice.setAddedAttributes(device.getAddedAttributes());
            tempMap.put(tempDevice.getEngNameOfDevice(), tempDevice);
        }

        List<Device> categoryDevices = new ArrayList<Device>(tempMap.values());
        setDeviceCategories(deviceList);

        healthCenter.setCategory_devices2(categoryDevices);
    }

    public void setDeviceCategories(List<Device> deviceList)
    {
        for (Device device : deviceList)
        {
            JSONObject tempMap = new JSONObject();

            for (Attribute attribute : device.getAllAttributes())
            {
                tempMap.put(attribute.getCategory() , attribute.getCategory());
            }

            device.setCategories(new ArrayList<Attribute>(tempMap.values()).toArray());
        }
    }

    public void processDevices(List<Device> deviceList , HealthCenter healthCenter)
    {
        for (Device device : deviceList)
        {
            for(Device categoryDevice : healthCenter.getCategory_devices())
            {
                if(device.getEngNameOfDevice().equalsIgnoreCase(categoryDevice.getEngNameOfDevice()))
                {
                    categoryDevice.size++;
                    if (device.getResult().equalsIgnoreCase("pass"))
                    {
                        healthCenter.pass++;
                        categoryDevice.pass++;
                    }
                    else if (device.getResult().equalsIgnoreCase("fail"))
                    {
                        healthCenter.fail++;
                        categoryDevice.fail++;
                    }
                    else if (device.getResult().equalsIgnoreCase("conditional"))
                    {
                        healthCenter.cond++;
                        categoryDevice.cond++;
                    }
                }
            }
        }
    }

    public boolean removeCurrentHC() throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpDelete request = new HttpDelete(HC_URL+ this.mainHC_id + "/");
        request.addHeader("Authorization","Token " + this.token);
        request.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpClient.execute(request);

        try
        {
            String result = null;
            System.out.println(response);
            if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
            {
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return false;
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } finally {
            httpClient.close();
        }

        return true;
    }

    public void updateHC(HealthCenter center) throws Exception
    {
        String result = "";

        HttpPut post = new HttpPut(HC_URL + center.id + "/");
        post.addHeader("Authorization", "Token " + token);
        post.setHeader("Content-type", "application/json");

        JSONObject temp = new JSONObject();
        temp.put("name" , center.getName());
        temp.put("address" , center.getAddress());
        temp.put("city" , center.getCity());
        temp.put("serial" , center.getSerial());

        // send a JSON data
        post.setEntity(new StringEntity(temp.toString(), "UTF-8"));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }
        catch (UnknownHostException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("There is problem with server!");
        }
    }

    public boolean removeHC(HealthCenter center) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpDelete request = new HttpDelete(HC_URL+ center.id + "/");
        request.addHeader("Authorization","Token " + this.token);

        CloseableHttpResponse response = httpClient.execute(request);

        try
        {
            String result = null;

/*            if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
            {
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return false;
            }*/

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
            }

        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } finally {
            httpClient.close();
        }

        return true;
    }

    public boolean removeDevice(Device removingDevice) throws Exception
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpDelete request = new HttpDelete(DEVICE_URL+ removingDevice.getId() + "/");
        request.addHeader("Authorization","Token " + this.token);

        CloseableHttpResponse response = httpClient.execute(request);

        try
        {
            String result = null;

/*            if(!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok"))
            {
                System.out.println(response.toString());
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return false;
            }*/

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        } catch (ClientProtocolException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } catch (IOException e)
        {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return false;
        } finally {
            httpClient.close();
        }

        return true;
    }

    public void setMainHC_id(int mainHC_id) {
        this.mainHC_id = mainHC_id;
    }

    public List<NewsItem> getNews() throws Exception
    {
        List<NewsItem> news = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet(NEWS_URL);

            // add request headers
            request.addHeader("Authorization","Token " + this.token);

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                String result = null;

                if (!response.getStatusLine().getReasonPhrase().equalsIgnoreCase("ok")) {
                    Utils utils = new Utils();
                    utils.make_WarningAlert("there is a problem with server!");
                    return null;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }

                /////////////////////////////////////////////////

                JSONParser parser = new JSONParser();
                JSONArray array = null;

                array = (JSONArray) parser.parse(result);

                for (Object object : array) {
                    JSONObject jsonObject = (JSONObject) object;

                    NewsItem item = new NewsItem();

                    item.setId(Integer.parseInt(jsonObject.get("id").toString()));
                    item.setTitle(jsonObject.get("title").toString());
                    item.setContent(jsonObject.get("text").toString());
                    item.setDate(DateConverter.getDate(jsonObject.get("date").toString().substring(0, 11)));

                    news.add(item);
                }

                /////////////////////////////////////////////////

            } catch (ParseException e) {
                Utils utils = new Utils();
                utils.make_WarningAlert("there is a problem with server!");
                return null;
            }
        } catch (IOException e) {
            Utils utils = new Utils();
            utils.make_WarningAlert("there is a problem with server!");
            return null;
        } finally {
            httpClient.close();
        }

        Collections.reverse(news);

        return news;
    }
}

