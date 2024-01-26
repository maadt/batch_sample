package com.example.batchprocessing;

// データ変換用クラスです
public class Employee {
    private String name;

    private String department;

    // コンストラクタを２つ定義します
    public Employee() {

    }

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    // 以下はすべてアクセサメソッド
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // ログ出力のためtoStringを実装します
    @Override
    public String toString() {
        return "name: " + this.name + " department: " + this.department;
    }
}