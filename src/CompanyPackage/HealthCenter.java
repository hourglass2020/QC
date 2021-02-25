package CompanyPackage;

import DeviecPackage.Device;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class HealthCenter
{
    public int id = 0;

    private String center_name;
    private String date;
    private String city;
    private String address;
    private String serial;

    List<Device> inputDevices = new ArrayList<>();

    List<Device> category_devices = new ArrayList<>();

    private Device mainDevice;

    public int pass = 0;
    public int fail = 0;
    public int cond = 0;

    public HealthCenter()
    {
        this.center_name = null;
        this.date = null;
        this.city = null;
        this.address = null;
        this.serial = null;
        this.mainDevice = null;
    }

    public HealthCenter(String name, String date, String city, String address , String serial) {
        this.center_name = name;
        this.date = date;
        this.city = city;
        this.address = address;
        this.serial = serial;
        this.mainDevice = null;
    }

    ////////////////////////////////////////////////////

    public String getName() {
        return center_name;
    }

    public void setName(String center) {
        this.center_name = center;
    }

    ////////////////////////////////////////////////////

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    ////////////////////////////////////////////////////

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    ////////////////////////////////////////////////////

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    ////////////////////////////////////////////////////

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    //////////////////////////////////////////////////////////////////////

    public void addCategoryDevice(Device device)
    {
        category_devices.add(device);
    }

    public void removeCategoryDevice(Device device)
    {
        category_devices.remove(device);
    }

    public Device getCategoryDevice(int position)
    {
        return category_devices.get(position);
    }

    ////////////////////////////////////////////////////

    public Device getMainDevice() {
        return mainDevice;
    }

    public void setMainDevice(Device mainDevice) {
        this.mainDevice = mainDevice;
    }


    public void initMainDevice()
    {
        mainDevice = new Device();
    } // **************** write later

    //////////////////////////////////////////////////////////////////////

    public void addInputDevice(Device device)
    {
        inputDevices.add(device);
    }

    public void removeInputDevice(Device device)
    {
        inputDevices.remove(device);
    }

    public Device getInputDevice(int position)
    {
        return (Device) inputDevices.get(position);
    }

    ////////////////////////////////////////////////////

    public void setCategory_devices(ObservableList<String> names)
    {
        List<Device> deviceList = new ArrayList<>();

        if(names != null) {
            for (String name : names) {
                deviceList.add(getDevice(name));
            }
        }

        category_devices.clear();
        category_devices = deviceList;
    }


    public void setCategory_devices2(List<Device> names)
    {
        category_devices = names;
    }

    public Device getDevice(String name)
    {
        for(Device device : Company.getCompany().getDevices())
        {
            if(device.getEngNameOfDevice().equalsIgnoreCase(name))
                return device;
        }
        return null;
    }

    public Device getPassDevice(String pass)
    {
        for(Device device : Company.getCompany().getMainHealthCenter().getInputDevices())
        {
            if(device.getSerialNo().equalsIgnoreCase(pass))
                return device;
        }
        return null;
    }

    public List<Device> getCategory_devices() {
        return category_devices;
    }

//    public void setCategory_devices(List<Device> category_devices) {
//        this.category_devices = category_devices;
//    }


    public List<Device> getInputDevices() {
        return inputDevices;
    }

    public void setInputDevices(List<Device> inputDevices) {
        this.inputDevices = inputDevices;
    }

    public void clear()
    {
        this.center_name = null;
        this.date = null;
        this.city = null;
        this.address = null;
        this.serial = null;
        this.mainDevice = null;
        this.category_devices = null;
    }
}
