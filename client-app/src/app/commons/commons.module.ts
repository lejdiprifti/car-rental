import { TableModule } from 'primeng/table';
import { ContextMenuModule } from 'primeng/contextmenu';
import * as core from '@angular/core';
import {
    InputTextModule,
    CalendarModule,
    ChipsModule,
    DropdownModule,
    KeyFilterModule,
    ListboxModule,
    MultiSelectModule,
    RadioButtonModule,
    SliderModule,
    SelectButtonModule,
    TriStateCheckboxModule,
    AutoCompleteModule,
    CheckboxModule,
    ColorPickerModule,
    EditorModule,
    InputSwitchModule,
    InputTextareaModule,
    InputMaskModule,
    PasswordModule,
    RatingModule,
    SpinnerModule,
    ToggleButtonModule,
    ButtonModule,
    SplitButtonModule,
    ChartModule,
    ConfirmDialogModule
} from 'primeng/primeng';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';

import { InputComponent } from '@ikubinfo/commons/input/input.component';
import { PanelComponent } from '@ikubinfo/commons/panel/panel.component';

@core.NgModule({
    imports: [CommonModule],
    exports: [
        InputTextModule,
        CalendarModule,
        ChipsModule,
        DropdownModule,
        KeyFilterModule,
        MultiSelectModule,
        RadioButtonModule,
        SliderModule,
        ListboxModule,
        SelectButtonModule,
        TriStateCheckboxModule,
        AutoCompleteModule,
        CheckboxModule,
        ColorPickerModule,
        EditorModule,
        InputSwitchModule,
        InputTextareaModule,
        InputMaskModule,
        PasswordModule,
        RatingModule,
        SpinnerModule,
        ToggleButtonModule,
        ButtonModule,
        SplitButtonModule,
        ReactiveFormsModule,
        ChartModule,
        TableModule,
        CommonModule,
        ContextMenuModule,
        ToastModule,
        ConfirmDialogModule,
        InputComponent,
        PanelComponent],
    declarations: [
        InputComponent,
        PanelComponent],
    providers: [],
})
export class CommonsModule { }
