package com.cnpc.test.entity;

import java.io.Serializable;

public class Person implements Serializable{
		 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String name;
		 private String sex;
		 private int age;
		 private String addass;
		public Person() {
			super();
		}
		public Person(String name, String sex, int age, String addass) {
			super();
			this.name = name;
			this.sex = sex;
			this.age = age;
			this.addass = addass;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getAddass() {
			return addass;
		}
		public void setAddass(String addass) {
			this.addass = addass;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", sex=" + sex + ", age=" + age + ", addass=" + addass + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((addass == null) ? 0 : addass.hashCode());
			result = prime * result + age;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((sex == null) ? 0 : sex.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Person other = (Person) obj;
			if (addass == null) {
				if (other.addass != null)
					return false;
			} else if (!addass.equals(other.addass))
				return false;
			if (age != other.age)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (sex == null) {
				if (other.sex != null)
					return false;
			} else if (!sex.equals(other.sex))
				return false;
			return true;
		}
		
		
}
