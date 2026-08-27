package kr.co.koscom.minibank.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.dto.TransactionHistoryResponseDto;
import kr.co.koscom.minibank.service.AccountService;
import kr.co.koscom.minibank.service.CustomerService;
import kr.co.koscom.minibank.service.TransferService;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import java.time.format.DateTimeFormatter;

@Route("accounts")
public class AccountListView extends VerticalLayout {
    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransferService transferService;
    private final Grid<AccountResponseDto> grid = new Grid<>(AccountResponseDto.class, false);

    public AccountListView(AccountService accountService, CustomerService customerService, TransferService transferService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transferService = transferService;

        // --- 1. 상단 타이틀 및 설명 ---
        H2 title = new H2("계좌 목록");
        title.getStyle().set("margin", "0");
        Span subtitle = new Span("개설된 모든 계좌의 잔액 및 예금주 정보를 조회합니다.");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        VerticalLayout headerLayout = new VerticalLayout(title, subtitle);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);

        // --- 2. 액션 버튼 영역 ---
        Button createButton = new Button("새 계좌 개설", e -> openCreateDialog());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // 강조 색상 적용

        Button transferButton = new Button("계좌 이체", e -> openTransferDialog());


        Button refreshButton = new Button("새로고침", e -> refreshGrid());
        Button toCustomers = new Button("고객 관리로 이동", e -> getUI().ifPresent(ui -> ui.navigate("customers")));

        HorizontalLayout actionLayout = new HorizontalLayout(createButton, refreshButton, transferButton,toCustomers);
        actionLayout.setAlignItems(Alignment.CENTER);

        // --- 3. 그리드 설정 ---
        grid.addColumn(AccountResponseDto::getAccountNumber).setHeader("계좌번호");
        grid.addColumn(AccountResponseDto::getOwnerName).setHeader("예금주");
        grid.addColumn(AccountResponseDto::getBalance).setHeader("잔액");
        grid.setWidthFull();
        grid.addThemeName("row-stripes");

        grid.addItemClickListener(event -> openTransactionDialog(event.getItem()));

        // --- 4. 메인 레이아웃 조립 ---
        add(headerLayout, actionLayout, grid);

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        refreshGrid();
    }

    private void refreshGrid() {
        grid.setItems(accountService.getAll());
    }

    private void openCreateDialog() {
        var customers = customerService.getAll();
        AccountFormDialog dialog = new AccountFormDialog(customers, accountService, this::refreshGrid);
        dialog.open();
    }

    private void openTransferDialog() {
        var accounts = accountService.getAll();
        if (accounts.size() < 2) {
            Notification.show("이체하려면 계좌가 최소 2개 이상 필요합니다.");
            return;
        }
        TransferFormDialog dialog = new TransferFormDialog(accounts, transferService, this::refreshGrid);
        dialog.open();
    }
    private void openTransactionDialog(AccountResponseDto account) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(account.getAccountNumber() + " 거래내역");

        Grid<TransactionHistoryResponseDto> txGrid = new Grid<>(TransactionHistoryResponseDto.class, false);

        txGrid.addColumn(new ComponentRenderer<>(tx -> {
            String type = tx.getType().name(); // 예: "DEPOSIT", "WITHDRAWAL" 또는 "입금", "출금"
            Span badge = new Span();
            badge.getStyle().set("padding", "2px 8px");
            badge.getStyle().set("border-radius", "4px");
            badge.getStyle().set("font-size", "12px");
            badge.getStyle().set("font-weight", "bold");

            if ("DEPOSIT".equalsIgnoreCase(type) || "입금".equals(type)) {
                badge.setText("입금");
                badge.getStyle().set("color", "var(--lumo-primary-text-color)");
                badge.getStyle().set("background-color", "var(--lumo-primary-color-10pct)");
            } else {
                badge.setText("출금");
                badge.getStyle().set("color", "var(--lumo-error-text-color)");
                badge.getStyle().set("background-color", "var(--lumo-error-color-10pct)");
            }
            return badge;
        })).setHeader("구분").setWidth("100px").setFlexGrow(0);

        txGrid.addColumn(new ComponentRenderer<>(tx -> {
            String type = tx.getType().name();
            Long amount = tx.getAmount().longValue();

            Span amountSpan = new Span();
            amountSpan.getStyle().set("font-weight", "600");

            boolean isDeposit = "DEPOSIT".equalsIgnoreCase(type) || "입금".equals(type);

            if (isDeposit) {
                amountSpan.setText("+ " + String.format("%,d", amount) + "원");
                amountSpan.getStyle().set("color", "var(--lumo-primary-text-color)");
            } else {
                amountSpan.setText("- " + String.format("%,d", amount) + "원");
                amountSpan.getStyle().set("color", "var(--lumo-error-text-color)");
            }
            return amountSpan;
        })).setHeader("금액").setWidth("140px").setFlexGrow(0);
        // 거래 후 잔액 콤마 포맷팅 추가
        txGrid.addColumn(t -> String.format("%,d원", t.getBalanceAfter().toBigInteger()))
                .setHeader("거래후 잔액").setWidth("140px").setFlexGrow(0);

        // 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        txGrid.addColumn(t -> t.getTransactionAt() != null ? t.getTransactionAt().format(formatter) : "")
                .setHeader("거래일시").setWidth("150px").setFlexGrow(0);

        txGrid.setItems(transferService.getHistoriesByAccountId(account.getId()));
        txGrid.setWidthFull();
        txGrid.setAllRowsVisible(true);

        // 닫기 버튼 추가
        Button closeButton = new Button("닫기", e -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        VerticalLayout dialogLayout = new VerticalLayout(txGrid, closeButton);
        dialogLayout.setAlignItems(Alignment.END);
        dialogLayout.setPadding(false);

        dialog.add(dialogLayout);
        dialog.setWidth("900px");
        dialog.open();
    }
}