package kr.co.koscom.minibank.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import kr.co.koscom.minibank.dto.AccountCreateRequestDto;
import kr.co.koscom.minibank.dto.CustomerResponseDto;
import kr.co.koscom.minibank.service.AccountService;
import java.math.BigDecimal;
import java.util.List;
public class AccountFormDialog extends Dialog {
    public AccountFormDialog(List<CustomerResponseDto> customers,
                             AccountService accountService, Runnable onSaved) {
        setHeaderTitle("새 계좌 개설");
        // 🔥 ID를 직접 입력하는 대신, 이름을 보고 고르는 ComboBox 사용
        ComboBox<CustomerResponseDto> customerBox = new ComboBox<>("고객 선택");
        customerBox.setItems(customers);
        customerBox.setItemLabelGenerator(CustomerResponseDto::getName);
        customerBox.setRequiredIndicatorVisible(true);
        customerBox.setWidthFull();
        if (customers.isEmpty()) {
            customerBox.setPlaceholder("등록된 고객이 없습니다. 먼저 '고객 관리'에서 등록하세요.");
            customerBox.setEnabled(false);
        }
        NumberField balanceField = new NumberField("초기 입금액");
        balanceField.setValue(0.0);
        balanceField.setMin(0);
        Button saveButton = new Button("저장", e -> {
            CustomerResponseDto selected = customerBox.getValue();
            if (selected == null) {
                Notification.show("고객을 선택해주세요.");
                return;
            }
            Double balanceValue = balanceField.getValue();
            AccountCreateRequestDto dto = new AccountCreateRequestDto();
            dto.setCustomerId(selected.getId());
            dto.setInitialBalance(BigDecimal.valueOf(balanceValue == null ? 0.0 :
                    balanceValue));
            try {
                accountService.createAccount(dto);
                onSaved.run();
                close();
            } catch (Exception ex) {
                Notification.show("오류 발생: " + ex.getMessage());
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // 강조 색상 적용
        Button cancelButton = new Button("취소", e -> close());
        add(new FormLayout(customerBox, balanceField));
        getFooter().add(cancelButton, saveButton);
    }
}