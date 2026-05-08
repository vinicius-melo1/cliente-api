package br.com.senac.clientes_api.services;

import br.com.senac.clientes_api.dtos.ClientesFiltroDto;
import br.com.senac.clientes_api.dtos.ClientesRequestDto;
import br.com.senac.clientes_api.entidades.Clientes;
import br.com.senac.clientes_api.repositorios.ClientesRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientesService {
    private ClientesRepositorio clientesRepositorio;

    public ClientesService(ClientesRepositorio clientesRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
    }

    public List<Clientes> listar (ClientesFiltroDto filtro) {
        if(filtro.getNome() != null) {
            return clientesRepositorio.findByNomeContaining(filtro.getNome());
        }
        if(filtro.getEmail() != null) {
            return clientesRepositorio.findByEmail(filtro.getEmail());
        }
        if(filtro.getIdade() > 0) {
            return clientesRepositorio.findByIdadeGreaterThan(filtro.getIdade());
        }

        return clientesRepositorio.findAll();
    }

    public Clientes listarById(Long id) {
        Optional<Clientes> retorno = clientesRepositorio.findById(id);
        if(retorno.isPresent()){
            return retorno.get();
        }

        throw new RuntimeException("Cliente não existe!");
    }

    public Clientes criar (ClientesRequestDto cliente) {
        Clientes clientePersist = this.clientesRequestDtoParaClientes(cliente);

        return clientesRepositorio.save(clientePersist);
    }

    public Clientes atualizar (Long id, ClientesRequestDto cliente) {
        if(clientesRepositorio.existsById(id)) {
            Clientes clientePersist = this.clientesRequestDtoParaClientes(cliente);

            clientePersist.setId(id);

            return clientesRepositorio.save(clientePersist);
        }

        throw new RuntimeException("Usuário Não encontrado");
    }

    public void deletar (Long id) {
        if(clientesRepositorio.existsById(id)) {
            clientesRepositorio.deleteById(id);
        }
        throw new RuntimeException("Cliente não encontrado");
    }

    private Clientes clientesRequestDtoParaClientes(ClientesRequestDto entrada) {
        Clientes saida = new Clientes();
        saida.setNome(entrada.getNome());
        saida.setDocumento(entrada.getDocumento());
        saida.setEmail(entrada.getEmail());

        return saida;
    }
}
