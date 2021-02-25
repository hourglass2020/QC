package Utilities;

import CompanyPackage.Company;
import CompanyPackage.HealthCenter;
import CompanyPackage.Operator;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class OfflineDataSaving
{
    File dir;

    public void saveProject(HealthCenter center)
    {
        dir = new File("projects/" + center.getName());
        if (!dir.exists()){
            dir.mkdirs();
        }

        String filename = getFileName(center.getName());

        JSONObject object = makeStructure(center);
        File file = makeFile(dir , filename);

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(object.toJSONString());
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getFileName(String nameFile)
    {
        String mainName = nameFile;
        boolean isOk = false;
        int index = 1;
        String tempName = mainName;

        while (!isOk)
        {
            File file = new File(dir , (tempName + ".json"));

            if(file.exists())
            {
                tempName = mainName + index;
                index++;
            }
            else
            {
                mainName = tempName;
                isOk = true;
            }
        }

        return mainName;
    }

    public File makeFile(File dir , String fileName)
    {
        File file = new File(dir , fileName + ".json");
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write("jsonText");
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return file;
    }

    public JSONObject makeStructure(HealthCenter center)
    {
        JSONObject temp = new JSONObject();
        temp.put("name" , center.getName());
        temp.put("address" , center.getAddress());
        temp.put("city" , center.getCity());
        temp.put("serial" , center.getSerial());
        temp.put("operator" , Operator.getOperator().getId());
        temp.put("Device" , getDeviceArray(center));

        return temp;
    }

    private JSONArray getDeviceArray(HealthCenter center)
    {
        JSONArray deviceArray = new JSONArray();

        for (Device device: center.getInputDevices())
        {
            JSONObject temp = new JSONObject();
            temp.put("persian_name" , device.getPerNameOfDevice());
            temp.put("english_name" , device.getEngNameOfDevice());
            temp.put("serial_number" , device.getSerialNo());
            temp.put("iso_number" , device.getIsoNo());
            temp.put("result" , device.getResult());
            temp.put("company" , Company.getCompany().getId());
            temp.put("attribute" , getAttributeArray(device));

            deviceArray.add(temp);
        }

        return deviceArray;
    }

    private JSONArray getAttributeArray(Device device)
    {
        JSONArray attArray = new JSONArray();

        for(Attribute attribute : device.getAllAttributes())
        {
            String choices = "";

            if (attribute.getChoicesNames() == null || attribute.getChoicesNames().isEmpty())
            {
                choices = "N/A";
            }
            else
            {
                choices = getAttributeChoices(attribute);
            }

            JSONObject temp = new JSONObject();
            temp.put("name", attribute.getName());
            temp.put("value", attribute.getMainValue());
            temp.put("mode", attribute.getMode());
            temp.put("max_value", attribute.getMax());
            temp.put("min_value", attribute.getMin());
            temp.put("category", attribute.getCategory());
            temp.put("choices", choices);
            temp.put("automatic_fill", attribute.isAutomaticFill());

            attArray.add(temp);
        }

        return attArray;
    }

    private String getAttributeChoices(Attribute attribute)
    {
        String choices = null;

        for (Object choice : attribute.getChoicesNames())
        {
            choice += (choice.toString() + ";");
        }

        return choices;
    }
}
