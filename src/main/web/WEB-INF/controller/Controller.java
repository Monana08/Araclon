package ficheros.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ficheros.dao.ClienteDAO;
import ficheros.dao.ProductoDAO;
import ficheros.dao.VentaDAO;
import ficheros.model.Cliente;
import ficheros.model.Producto;
import ficheros.model.Venta;

@WebServlet("/intro")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductoDAO productoDao = new ProductoDAO();
	private ClienteDAO clienteDao = new ClienteDAO();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private VentaDAO ventaDao = new VentaDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String accion = request.getParameter("accion");
		if (accion.equalsIgnoreCase("agregar")) {

			List<Producto> productos = productoDao.obtenerTodos();
			List<Cliente> clientes = clienteDao.obtenerTodos();
			request.setAttribute("prod", productos);
			request.setAttribute("cli", clientes);
			request.getRequestDispatcher("acciones/NVenta.jsp").forward(request, response);
		}
		else if (accion.equalsIgnoreCase("listar")) {
			List<Venta> ventas = ventaDao.obtenerTodos();
			request.setAttribute("ventap", ventas);
			request.getRequestDispatcher("acciones/Listar.jsp").forward(request, response);
		}
		else if (accion.equalsIgnoreCase("consultar")){
			int id = Integer.parseInt(request.getParameter("id"));
			Venta venta = ventaDao.obtenerPorId(id);
			request.setAttribute("ventap", venta);
			request.getRequestDispatcher("acciones/Consulta.jsp").forward(request, response);
		}
		else if (accion.equalsIgnoreCase("eliminar")) {
			int id = Integer.parseInt(request.getParameter("id"));
			ventaDao.eliminar(id);
			response.sendRedirect("intro?accion=listar");
		}
		else if (accion.equalsIgnoreCase("editar")) {
			int id = Integer.parseInt(request.getParameter("id"));
			Venta venta = ventaDao.obtenerPorId(id);
			List<Producto> productos = productoDao.obtenerTodos();
			List<Cliente> clientes = clienteDao.obtenerTodos();
			request.setAttribute("prod", productos);
			request.setAttribute("cli", clientes);
			request.setAttribute("ventap", venta);
			request.getRequestDispatcher("acciones/Editar.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String accion = request.getParameter("accion");
		if (accion.equalsIgnoreCase("nuevaven")) {
			int idCliente = Integer.parseInt(request.getParameter("cli"));
			int idProducto = Integer.parseInt(request.getParameter("prod"));
			double precio = Double.parseDouble(request.getParameter("pre"));
			int cantidad = Integer.parseInt(request.getParameter("cant"));
			String formaPago = request.getParameter("pago");
			Date fecha = null;
			try {
				fecha = sdf.parse(request.getParameter("fecha"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			boolean aprobada = (request.getParameter("aprob") == null ? false : true);
			Venta venta = new Venta(0, idCliente, null, idProducto,null, precio, cantidad, formaPago, fecha, aprobada);
			System.out.print(venta);
			ventaDao.agregar(venta);
			request.getRequestDispatcher("index.html").forward(request, response);

		}
		else if (accion.equalsIgnoreCase("modificarVenta")) {
			int id = Integer.parseInt(request.getParameter("id"));
			int idCliente = Integer.parseInt(request.getParameter("cliente"));
			int idProducto = Integer.parseInt(request.getParameter("producto"));
			double precio = Double.parseDouble(request.getParameter("precio"));
			int cantidad = Integer.parseInt(request.getParameter("cantidad"));
			String formaPago = request.getParameter("pago");
			Date fecha = null;
			try {
				fecha = sdf.parse(request.getParameter("fecha"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			boolean aprobada = (request.getParameter("aprobado") == null ? false : true);
			Venta venta = new Venta(id, idCliente, null, idProducto,null, precio, cantidad, formaPago, fecha, aprobada);
			System.out.print(venta);
			ventaDao.actualizar(venta);
			List<Venta> ventas = ventaDao.obtenerTodos();
			request.setAttribute("ventap", ventas);
			request.getRequestDispatcher("acciones/Listar.jsp").forward(request, response);
		}

	}
}
