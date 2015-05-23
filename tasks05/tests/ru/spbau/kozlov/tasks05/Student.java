package ru.spbau.kozlov.tasks05;

import checkers.nullness.quals.Nullable;

/**
 * @author adkozlov
 */
public class Student {

    @Nullable
    private String name;
    @Nullable
    private String surname;
    private int age;
    private char averageMark;

    private boolean scholarship;

    public Student() {
    }

    public Student(@Nullable String name, @Nullable String surname, int age, char averageMark, boolean scholarship) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.averageMark = averageMark;
        this.scholarship = scholarship;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(char averageMark) {
        this.averageMark = averageMark;
    }

    private boolean getScholarship() {
        return scholarship;
    }

    private void setScholarship(boolean scholarship) {
        this.scholarship = scholarship;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", averageMark=" + averageMark +
                ", scholarship=" + scholarship +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;
        return age == student.age
                && averageMark == student.averageMark
                && scholarship == student.scholarship
                && !(name != null ? !name.equals(student.name) : student.name != null)
                && !(surname != null ? !surname.equals(student.surname) : student.surname != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (int) averageMark;
        result = 31 * result + (scholarship ? 1 : 0);
        return result;
    }
}
