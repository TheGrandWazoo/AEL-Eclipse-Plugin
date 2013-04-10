package ael.editors;

public class Keywords implements Comparable<Keywords>
{
	private String value;
	private String description;

	public Keywords(String Value, String Description)
	{
		super();
		this.value = Value;
		this.description = Description;
	}
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * compareTo method
	 */
	public int compareTo(Keywords otherKeyword)
	{
		if (!(otherKeyword instanceof Keywords)) {
			throw new ClassCastException("Invalid object");
		}
		return 1;
	}
}
