package net.skhu.javaproject_address;

public class Person {
    private String name,age,email,number;
    public Person(){}
    public Person(String name,String age, String email ,String number){
        this.name = name;
        this.age = age;
        this.number = number;
        this.email = email;

    }

    public String getName() {
        return this.name;
    }

    public String getAge() {
        return this.age;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNumber() {
        return this.number;
    }
}
