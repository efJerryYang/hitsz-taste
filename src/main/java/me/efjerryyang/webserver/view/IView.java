package me.efjerryyang.webserver.view;

import java.util.List;

public interface IView {

    IView createView();

    void show();

    void showHeader();

    void showList(List<IView> iViewList);
}
