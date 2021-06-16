package com.es.phoneshop.web;

import com.es.phoneshop.model.recentviews.RecentView;
import com.es.phoneshop.model.recentviews.RecentViewService;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private RecentViewService recentViewService;
    @Mock
    private HttpSession session;

    private ProductListPageServlet servlet = new ProductListPageServlet();

    @SneakyThrows
    @Before
    public void setup() {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(recentViewService.getRecentView(request.getSession())).thenReturn(new RecentView());
        when(request.getParameter(RequestParameter.QUERY)).thenReturn("Siemens");
        when(request.getParameter(RequestParameter.SORT)).thenReturn(String.valueOf(SortField.DESCRIPTION));
        when(request.getParameter(RequestParameter.ORDER)).thenReturn(String.valueOf(SortOrder.ASC));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq(RequestParameter.PRODUCTS), any());
        verify(request).setAttribute(eq(RequestParameter.RECENT_VIEWS), any());
    }


}