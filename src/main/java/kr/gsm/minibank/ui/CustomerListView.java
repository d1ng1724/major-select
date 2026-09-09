package kr.gsm.minibank.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import kr.gsm.minibank.dto.CustomerResponseDto;
import kr.gsm.minibank.service.CustomerService;

@Route("customers")
public class CustomerListView extends VerticalLayout {
    private final CustomerService customerService;
    private final Grid<CustomerResponseDto> grid = new Grid<>(CustomerResponseDto.class, false);

    public CustomerListView(CustomerService customerService) {
        this.customerService = customerService;

        // --- 1. 상단 타이틀 및 설명 ---
        H2 title = new H2("고객 관리");
        title.getStyle().set("margin", "0");
        Span subtitle = new Span("미니뱅크에 등록된 고객 정보를 조회하고 관리합니다.");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        VerticalLayout headerLayout = new VerticalLayout(title, subtitle);
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);

        // --- 2. 액션 버튼 영역 ---
        Button createButton = new Button("새 고객 등록", e -> openCreateDialog());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // 강조 색상 적용

        Button refreshButton = new Button("새로고침", e -> refreshGrid());
        Button toAccounts = new Button("계좌 목록으로 이동", e -> getUI().ifPresent(ui -> ui.navigate("accounts")));

        HorizontalLayout actionLayout = new HorizontalLayout(createButton, refreshButton, toAccounts);
        actionLayout.setAlignItems(Alignment.CENTER);

        // --- 3. 그리드 설정 ---
        grid.addColumn(CustomerResponseDto::getId).setHeader("고객 ID").setWidth("100px").setFlexGrow(0);
        grid.addColumn(CustomerResponseDto::getName).setHeader("이름");
        grid.addColumn(CustomerResponseDto::getPhone).setHeader("연락처");
        grid.setWidthFull();
        grid.addThemeName("row-stripes"); // 줄무늬 효과로 가독성 향상

        // --- 4. 메인 레이아웃 조립 ---
        add(headerLayout, actionLayout, grid);

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        refreshGrid();
    }

    private void refreshGrid() {
        grid.setItems(customerService.getAll());
    }

    private void openCreateDialog() {
        CustomerFormDialog dialog = new CustomerFormDialog(customerService, this::refreshGrid);
        dialog.open();
    }
}