ValueObjectValidation
=====================

This repository contains a complete sample application which shows how to integrate ValueObject validation and JSF/JavaEE.

Motivation
----------

Value Objects should always represent a valid instance of a value. While this is usually very easy, some problems may arise if you use value objects in your JSF Views. This example shows the usage of the Bean Validation Framework (JSR303) to prevent invalid objects and the handling of error messages.

Why?
----

To prevent non-valid Value Object instances, the Value Objects must check its constructor arguments. This is usually very easy. Let's look at the following example:

```
public final AccountId {
  private String value;
  
  public AccountId(String value){
    if(value==null||value.isEmpty()){
      throw new IllegalArgumentException("null or empty");
    }
    // propably more rules ...
  }
}
```

If one needs to use those Value Object in JSF Views, a Converter class has to be provided, so JSF known how to construct an instance out of a string. Since JSF validates after conversion the `Converter.getAsObject` Method must handle non-valid string values. This  could be done by calling the constructor of the requested Value Object. If the argument is non-valid, an exception is thrown.

This approach has a minor drawback: This code runs inside a web-application! A mechanism has to be provided to get the correct, language-specific error message. This could be done in a very convenient way, by using the Bean Validation Framework. So instead of checking each argument, we should declare validation constraints on the fields of the Value Object and use the framework provided by the platform:

```
public final AccountId {
	@NotNull
	@Size(min = 4, max = 10)
	@Pattern(regexp = "[A|B]+[0-9]+")
  private String value;
  
	public AccountId(String value) {
		this.value = value;
		BeanValidator.current().validate(this);
	}
}
```
