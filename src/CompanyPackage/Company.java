package CompanyPackage;

import DeviecPackage.Device;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Company
{
    private static Company company = new Company();
    private Company(){}
    public static Company getCompany(){ return company; }

    private int id;
    private String name;
    private String username = "";
    private String password = "";
    private String category = new String();
    private String phoneNo;
    private String email;
    private String address;

    private boolean access;

    private List<HealthCenter> healthCenters = new ArrayList<HealthCenter>();
    private HealthCenter mainHealthCenter = new HealthCenter();
    private List<Device> devices = new ArrayList<Device>();
    private List<Device> submitedDevices = new ArrayList<>();
    private Device mainDevice = new Device();
    private JSONArray deviceNames = new JSONArray();

    ///////////////////////////////////////////////////////////////////////

    public Device getMainDevice() {
        return mainDevice;
    }

    public void setMainDevice(Device mainDevice) {
        this.mainDevice = mainDevice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public List<Device> getSubmitedDevices() {
        return submitedDevices;
    }

    public void setSubmittedDevices(List<Device> submitedDevices) {
        this.submitedDevices = submitedDevices;
    }

    ///////////////////////////////////////////////////////////////////////

    public List<HealthCenter> getHealthCenters() {
        return healthCenters;
    }

    public void setHealthCenters(List<HealthCenter> healthCenters) {
        this.healthCenters = healthCenters;
    }

    public HealthCenter getMainHealthCenter() {
        return mainHealthCenter;
    }

    public void setMainHealthCenter(HealthCenter mainHealthCenter) {
        this.mainHealthCenter = mainHealthCenter;
    }

    public void addDevice(Device device)
    {
        devices.add(device);
    }

    public void removeDevice(Device device)
    {
        devices.remove(device);
    }

    public Device getDevice(int position)
    {
        return devices.get(position);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public ObservableList<String> getDeviceNames()
    {
        ObservableList<String> names = FXCollections.observableArrayList();

        for(Device device : devices)
        {
            names.add(device.getEngNameOfDevice());
        }
        return names;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public JSONArray getNames()
    {
//        JSONArray array = new JSONArray();
        if(deviceNames.isEmpty())
        {
            for (Device device : devices) {
                deviceNames.add(device.getEngNameOfDevice());
            }
        }

        return this.deviceNames;
    }

    public Device getPassDevice(String pass)
    {
        for(Device device : devices)
        {
            if(device.getEngNameOfDevice().equalsIgnoreCase(pass))
                return device;
        }
        return null;
    }

    public void setDeviceNames(JSONArray deviceNames) {
        this.deviceNames = deviceNames;
    }

}
