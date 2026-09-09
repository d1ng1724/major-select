package kr.gsm.minibank.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import kr.gsm.minibank.dto.AccountResponseDto;
import kr.gsm.minibank.dto.TransferRequestDto;
import kr.gsm.minibank.service.TransferService;

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

        // 보내는 계좌의 한도 및 금일 사용액 정보를 보여줄 텍스트 영역
        Span limitInfoLabel = new Span("계좌를 선택해주세요.");
        limitInfoLabel.getStyle().set("font-size", "var(--lumo-font-size-s)");
        limitInfoLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // 보내는 계좌가 바뀔 때마다 한도 정보 업데이트
        fromBox.addValueChangeListener(event -> {
            AccountResponseDto selected = event.getValue();
            if (selected != null) {
                // AccountResponseDto에 getDailyLimit(), getUsedAmount() 메서드가 있다고 가정합니다.
                // 만약 필드명이 다르다면 DTO에 맞춰 메서드명을 수정해주세요.
                limitInfoLabel.setText(String.format("일일 이체 한도: %s원 | 금일 사용액: %s원",
                        selected.getDailyLimit(), transferService.getUsedAmount(selected.getId())));
            } else {
                limitInfoLabel.setText("계좌를 선택해주세요.");
            }
        });

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

        add(new FormLayout(fromBox, limitInfoLabel, toBox, amountField));
        getFooter().add(cancelButton, saveButton);
    }
}
