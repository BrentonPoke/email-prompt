package models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Document{
	@JsonProperty("type")
	public String type;
	@JsonProperty("children")
	public ArrayList<ChildItem> children;
}