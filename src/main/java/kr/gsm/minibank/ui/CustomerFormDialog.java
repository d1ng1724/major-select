package kr.gsm.minibank.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import kr.gsm.minibank.dto.CustomerCreateDto;
import kr.gsm.minibank.service.CustomerService;


public class CustomerFormDialog extends Dialog {
    public CustomerFormDialog(CustomerService customerService, Runnable onSaved) {
        setHeaderTitle("새 고객 등록");
        TextField nameField = new TextField("이름");
        TextField phoneField = new TextField("연락처");
        nameField.setRequiredIndicatorVisible(true);
        Button saveButton = new Button("저장", e -> {
            if (nameField.isEmpty()) {
                Notification.show("이름은 필수입니다.");
                return;
            }
            CustomerCreateDto dto = new CustomerCreateDto();
            dto.setName(nameField.getValue());
            dto.setPhone(phoneField.getValue());
            customerService.create(dto);
            onSaved.run(); // 부모 화면(CustomerListView)의 목록 새로고침 실행
            close();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // 강조 색상 적용
        Button cancelButton = new Button("취소", e -> close());
        add(new FormLayout(nameField, phoneField));
        getFooter().add(cancelButton, saveButton);
    }
}