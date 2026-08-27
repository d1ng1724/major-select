package kr.co.koscom.minibank.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.dto.TransferRequestDto;
import kr.co.koscom.minibank.service.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class TransferFormDialog extends Dialog {

    public TransferFormDialog(List<AccountResponseDto> accounts,
                              TransferService transferService, Runnable onSaved) {
        setHeaderTitle("계좌 이체");

        ComboBox<AccountResponseDto> fromBox = new ComboBox<>("보내는 계좌");
        ComboBox<AccountResponseDto> toBox = new ComboBox<>("받는 계좌");
        fromBox.setItems(accounts);
        toBox.setItems(accounts);
        fromBox.setItemLabelGenerator(a ->
                a.getAccountNumber() + " (" + a.getOwnerName() + ", 잔액 " + a.getBalance() + ")");
        toBox.setItemLabelGenerator(fromBox.getItemLabelGenerator());
        fromBox.setWidthFull();
        toBox.setWidthFull();

        NumberField amountField = new NumberField("이체 금액");
        amountField.setMin(0);

        Button saveButton = new Button("이체", e -> {
            AccountResponseDto from = fromBox.getValue();
            AccountResponseDto to = toBox.getValue();
            Double amountValue = amountField.getValue();

            if (from == null || to == null) {
                Notification.show("보내는 계좌와 받는 계좌를 모두 선택해주세요.");
                return;
            }
            if (from.getAccountNumber().equals(to.getAccountNumber())) {
                Notification.show("같은 계좌로는 이체할 수 없습니다.");
                return;
            }
            if (amountValue == null || amountValue <= 0) {
                Notification.show("이체 금액을 정확히 입력해주세요.");
                return;
            }

            TransferRequestDto request = new TransferRequestDto();
            request.setFromAccountNumber(from.getAccountNumber());
            request.setToAccountNumber(to.getAccountNumber());
            request.setAmount(BigDecimal.valueOf(amountValue));

            try {
                transferService.transfer(request);
                Notification.show("이체가 완료되었습니다.");
                onSaved.run();
                close();
            } catch (Exception ex) {
                Notification.show("이체 실패: " + ex.getMessage());
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("취소", e -> close());

        add(new FormLayout(fromBox, toBox, amountField));
        getFooter().add(cancelButton, saveButton);
    }
}
