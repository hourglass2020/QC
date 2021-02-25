package DeviecPackage;

import CompanyPackage.HealthCenter;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Device
{
    private int id = 0;
    private int owner = 0;
    private HealthCenter tempCenter = null;

    String engNameOfDevice;
    String perNameOfDevice;
    String serialNo;
    String isoNo;
    List<Attribute> addedAttributes = new ArrayList<Attribute>();
    List<Attribute> defaultAttributes = new ArrayList<>();
    String result;

    private List<String> categories = new ArrayList<>();

    Attribute mainAttribute = new Attribute();

    String[] defaultAttributesNames = {"سازنده" , "مدل" , "شماره اموال" ,
            "محل استقرار دستگاه", "کنترل کننده", "دلیل" , "رطوبت" , "فشار" , "دما"};

    public int size = 0;
    public int pass = 0;
    public int fail = 0;
    public int cond = 0;

    public Device(int id)
    {}

    public Device(String engNameOfDevice, String perNameOfDevice,
                  List<Attribute> addedAttributes, List<Attribute> defaultAttributes) {
        this.engNameOfDevice = engNameOfDevice;
        this.perNameOfDevice = perNameOfDevice;
        this.addedAttributes = addedAttributes;
        this.defaultAttributes = defaultAttributes;
    }

    public Device(Device device)
    {
        this.engNameOfDevice = device.engNameOfDevice;
        this.perNameOfDevice = device.perNameOfDevice;
        this.isoNo = device.isoNo;
//        setAddedAttributes(device.getAddedAttributes());
        setDefaultValues();
        this.categories = device.getCategories();
    }

    public Device()
    {
        setDefaultValues();
    }


    public void init(List<Attribute> cases)
    {
        for(Attribute attribute : cases)
        {
            this.addedAttributes.add(attribute);
        }
    }

    public String getEngNameOfDevice() {
        return engNameOfDevice;
    }

    public void setEngNameOfDevice(String engNameOfDevice) {
        this.engNameOfDevice = engNameOfDevice;
    }

    public String getPerNameOfDevice() {
        return perNameOfDevice;
    }

    public void setPerNameOfDevice(String perNameOfDevice) {
        this.perNameOfDevice = perNameOfDevice;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public HealthCenter getTempCenter() {
        return tempCenter;
    }

    public void setTempCenter(HealthCenter tempCenter) {
        this.tempCenter = tempCenter;
    }

    //////////////////////////////////////////////////

    public void addAttribute(Attribute newAttribute) { addedAttributes.add(newAttribute); }
    public void removeAttribute(Attribute attribute) { addedAttributes.remove(attribute); }
    public Attribute getAttribute(int pos) { return addedAttributes.get(pos); }

    ///////////////////////////////////////////////////////


    public Attribute getAttribute(String name)
    {
        for(Attribute attribute : getAllAttributes())
        {
            if(attribute.getName().equalsIgnoreCase(name))
                return attribute;
        }
        return null;
    }

    public Attribute getMainAttribute() {
        return mainAttribute;
    }

    public void setMainAttribute(Attribute mainAttribute) {
        this.mainAttribute = mainAttribute;
    }

    public List<Attribute> getAddedAttributes() {
        return addedAttributes;
    }

    ///////////////////////////////////////////////////////

    public void clear()
    {
        engNameOfDevice = null;
        perNameOfDevice = null;
        addedAttributes = null;
    }

    ///////////////////////////////////////////////////////

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    ///////////////////////////////////////////////////////

    private void setDefaultValues()
    {
        for(String val : defaultAttributesNames)
        {
            Attribute attribute = new Attribute(val, null , "Default" ,
                    0 , 0 , Attribute.TEXT_FIELD);
            defaultAttributes.add(attribute);
        }

        categories.add("Default");
    }

    public String[] getDefaultAttributesNames() {
        return defaultAttributesNames;
    }

    ///////////////////////////////////////////////////////


    public List<Attribute> getDefaultAttributes() {
        return defaultAttributes;
    }

    public List<Attribute> getAllAttributes()
    {
        List<Attribute> attributeList = new ArrayList<>();

        attributeList.addAll(defaultAttributes);
        attributeList.addAll(addedAttributes);

        return attributeList;
    }

    public void setAllAttributes(List<Attribute> allAttributes)
    {
        this.addedAttributes = allAttributes;
    }

    public void addDefaultAttribute(Attribute newAttribute) { defaultAttributes.add(newAttribute); }

    public void setAddedAttributes(List<Attribute> attributeList)
    {
        for (Attribute attribute : attributeList)
        {
            Attribute temp = new Attribute();
            temp.setChoices(attribute.getChoicesNames());
            temp.setDefaultValue(attribute.getDefaultValue());
            temp.setMin(attribute.getMin());
            temp.setMax(attribute.getMax());
            temp.setName(attribute.getName());
            temp.setCategory(attribute.getCategory());
            temp.setAutomaticFill(attribute.isAutomaticFill());
            temp.setMode(attribute.getMode());
            addedAttributes.add(temp);
        }
    }

    public void addCategory(String category) { categories.add(category); }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(JSONArray categories) {
        this.categories = categories;
    }

    public void setCategories(Object[] choices)
    {
        for (Object obj : choices)
            this.categories.add(obj.toString());
    }

    public void setDefaultAttributes(List<Attribute> defaultAttributeList) {
        this.defaultAttributes = defaultAttributeList;
    }
}
