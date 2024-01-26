package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {
	// implements ItemProcessor：必ず実装する
	// <Employee, Employee>：<入力側の型, 加工後の型>
	
    // ログ出力用のオブジェクトを生成します
	// Loggerクラスの宣言は任意ですが、コンソールにログを出力してくれるため実行結果がわかりやすいので用意しておきましょう。
    private static final Logger log = LoggerFactory.getLogger(EmployeeItemProcessor.class);

    // ItemProcessorの処理を定義します
    @Override // 必ずprocess()をオーバーライドする
    public Employee process(final Employee employee) throws Exception {
    	// Employee：返り値には加工後(ItemWriterに引き渡す)の型・値を指定する
    	// process()：引数には入力側(ItemReaderから渡される)の型・値

        // 名前をすべて大文字にします
        final String name = employee.getName().toUpperCase();
        // フィールドを差し替えた新しいEmployeeオブジェクトを生成します
        final Employee transformedEmployee = new Employee(name, employee.getDepartment());
        // 差分をログに出力します
        log.info("変換結果 (" + employee + ") => (" + transformedEmployee + ")");
        
        // 変換後のオブジェクトを返却します
        return transformedEmployee;
    }
}