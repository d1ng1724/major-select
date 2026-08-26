package kr.co.koscom.minibank.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.service.AccountService;
import kr.co.koscom.minibank.service.CustomerService;

@Route("accounts")
public class AccountListView extends VerticalLayout {
    private final AccountService accountService;
    private final CustomerService customerService;
    private final Grid<AccountResponseDto> grid = new Grid<>(AccountResponseDto.class, false);

    public AccountListView(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;

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

        Button refreshButton = new Button("새로고침", e -> refreshGrid());
        Button toCustomers = new Button("고객 관리로 이동", e -> getUI().ifPresent(ui -> ui.navigate("customers")));

        HorizontalLayout actionLayout = new HorizontalLayout(createButton, refreshButton, toCustomers);
        actionLayout.setAlignItems(Alignment.CENTER);

        // --- 3. 그리드 설정 ---
        grid.addColumn(AccountResponseDto::getAccountNumber).setHeader("계좌번호");
        grid.addColumn(AccountResponseDto::getOwnerName).setHeader("예금주");
        grid.addColumn(AccountResponseDto::getBalance).setHeader("잔액");
        grid.setWidthFull();
        grid.addThemeName("row-stripes");

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
}