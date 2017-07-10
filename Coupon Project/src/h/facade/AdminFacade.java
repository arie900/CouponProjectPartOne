package h.facade;


import java.util.LinkedList;
import java.util.List;

import d.company.Company;
import d.company.CompanyDBDAO;
import e.customer.Customer;
import e.customer.CustomerDBDAO;
import f.comp_coupon.Comp_coupnDBDAO;
import g.cust_coupon.Cust_CoupnDBDAO;
import i.couponSystem.CouponSystemException;

/**
 * this class is the Admin facade it has the highest permissions. 
 * the admin methods allow manipulation of the companies and the customers beans.
 *  the CRUD methods here are implemented on the costumer,Company,Coupon Tables.
 */
public class AdminFacade implements CouponClientFacade {

	private CustomerDBDAO customerdbdao = new CustomerDBDAO();
	private CompanyDBDAO companydbdao = new CompanyDBDAO();
	private Comp_coupnDBDAO comp_coupondbdao = new Comp_coupnDBDAO();
	private Cust_CoupnDBDAO cust_coupondbdao = new Cust_CoupnDBDAO();

	public AdminFacade() {
		super();
	}

	public void createCompany(Company company) throws CouponSystemException {
		List<Company> companyList = new LinkedList<>();
		companyList = companydbdao.getAllCompanies();
		for (Company company2 : companyList) {
			if (company2.getCompName().equals(company.getCompName())) {
				System.out.println("Company with the company name '" + company.getCompName() + "' already exist");
				return;
			}
		}
		companydbdao.createCompany(company);
	}

	public void removeCompany(Company company) throws CouponSystemException {
		if (companydbdao.getCompany(company.getId()) != null) {
			companydbdao.deleteCompany(company);
			comp_coupondbdao.deleteCompanyAllCoupons(company);
			
		}
      
	}

	public void updateCompany(Company comp) throws CouponSystemException {
		companydbdao.updateCompany(comp);
	}

	public List<Company> getAllCompanies() throws CouponSystemException {
		return companydbdao.getAllCompanies();
	}

	public Company getCompany(long id) throws CouponSystemException {
		return companydbdao.getCompany(id);
	}

	public void createCustomer(Customer customer) throws CouponSystemException {
		List<Customer> customerList = new LinkedList<>();
		customerList = customerdbdao.getAllCustomer();
		for (Customer customer2 : customerList) {
			if (customer2.getCustName().equals(customer.getCustName())) {
				System.out.println("Customer with the customer name '" + customer.getCustName() + "' already exist");
				return;
			}

		}
		customerdbdao.createCustomer(customer);

	}

	public void removeCustomer(Customer cust) throws CouponSystemException {
		if (customerdbdao.getCustomer(cust.getId()) != null) {
			customerdbdao.deleteCustomer(cust);
			cust_coupondbdao.deleteCustomerAllCoupons(cust);
			}
		
	}

	public void updateCustomer(Customer cust) throws CouponSystemException {
		customerdbdao.updateCustomer(cust);
	}

	public List<Customer> getAllCustomers() throws CouponSystemException {
		return customerdbdao.getAllCustomer();
	}

	public Customer getCustomer(String custName) throws CouponSystemException {
		return customerdbdao.getCustomerByName(custName);
	}
	
	public Customer getCustomerById(long id) throws CouponSystemException{
		return customerdbdao.getCustomer(id);
	}
	
	@Override
	public boolean login(String name, String password, Enum<ClientType> clientType) throws CouponSystemException {
		if (name.equals("admin") && password.equals("1234") && clientType.equals(ClientType.ADMIN)) {
			System.out.println("Admin logged in");
			return true;
		} else {
			return false;
		}

	}
}
